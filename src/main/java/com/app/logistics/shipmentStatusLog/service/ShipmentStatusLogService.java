    package com.app.logistics.shipmentStatusLog.service;
    
    import com.app.logistics.auth.authUtils.AuthDetails;
    import com.app.logistics.common.exception.APIException;
    import com.app.logistics.driver.entity.Driver;
    import com.app.logistics.driver.service.DriverService;
    import com.app.logistics.manager.entity.Manager;
    import com.app.logistics.manager.service.ManagerService;
    import com.app.logistics.operator.entity.Operator;
    import com.app.logistics.operator.service.OperatorService;
    import com.app.logistics.shipment.entity.Shipment;
    import com.app.logistics.shipment.service.ShipmentService;
    import com.app.logistics.shipmentStatusLog.dto.ShipmentStatusLogRequest;
    import com.app.logistics.shipmentStatusLog.dto.ShipmentStatusLogResponse;
    import com.app.logistics.shipmentStatusLog.repo.StatusLogRepo;
    import com.app.logistics.shipmentStatusLog.entity.ShipmentStatusLog;
    import com.app.logistics.shipmentStatusLog.utils.ShipmentStatusLogMapper;
    import com.app.logistics.vehicle.entity.Vehicle;
    import com.app.logistics.vehicle.service.VehicleService;
    import org.springframework.http.HttpStatus;
    import org.springframework.stereotype.Service;
    import org.springframework.transaction.annotation.Propagation;
    import org.springframework.transaction.annotation.Transactional;

    import java.math.BigDecimal;
    import java.time.LocalDateTime;
    import java.util.List;
    import java.util.Map;

    @Service
    public class ShipmentStatusLogService {

        private static final double ARRIVAL_RADIUS_METERS = 100.0;
    
        private final StatusLogRepo statusLogRepo;
        private final ManagerService managerService;
        private final ShipmentStatusLogMapper shipmentStatusLogMapper;
        private final ShipmentService shipmentService;
        private final OperatorService operatorService;
        private final DriverService driverService;
        private final VehicleService vehicleService;
    
        public ShipmentStatusLogService(StatusLogRepo statusLogRepo, ManagerService managerService, ShipmentStatusLogMapper shipmentStatusLogMapper, ShipmentService shipmentService, OperatorService operatorService, DriverService driverService, VehicleService vehicleService) {
            this.statusLogRepo = statusLogRepo;
            this.managerService = managerService;
            this.shipmentStatusLogMapper = shipmentStatusLogMapper;
            this.shipmentService = shipmentService;
            this.operatorService = operatorService;
            this.driverService = driverService;
            this.vehicleService = vehicleService;
        }
    
        @Transactional(propagation = Propagation.REQUIRED)
        public ShipmentStatusLog saveShipmentStatusLog(Operator operator,
                                                       Driver driver,
                                                       Vehicle vehicle,
                                                       Integer updatedBy,
                                                       Map<String,Object> currentLocationDetails,
                                                       Shipment shipment){
            if (shipment == null || updatedBy == null) {
                throw new APIException("Payload request and user context are required for creating status tracking states", HttpStatus.BAD_REQUEST);
            }
            ShipmentStatusLog savingShipmentStatusLog = new ShipmentStatusLog();
            savingShipmentStatusLog.setShipment(shipment);
            savingShipmentStatusLog.setCurrentLocation((String)currentLocationDetails.get("currentLocation"));
            savingShipmentStatusLog.setCurrentLatitude((BigDecimal)currentLocationDetails.get("currentLatitude"));
            savingShipmentStatusLog.setCurrentLongitude((BigDecimal)currentLocationDetails.get("currentLongitude"));
            savingShipmentStatusLog.setOperator(operator);
            savingShipmentStatusLog.setDriver(driver);
            savingShipmentStatusLog.setVehicle(vehicle);
            savingShipmentStatusLog.setUpdatedAt(LocalDateTime.now());
            savingShipmentStatusLog.setUpdatedBy(updatedBy);
    
            return statusLogRepo.save(savingShipmentStatusLog);
        }
    
        @Transactional(readOnly = true)
        public ShipmentStatusLogResponse fetchCurrentStatus(Integer shippingId) {
            ShipmentStatusLog fetchedShipmentStatusLog = statusLogRepo.findFirstByShipment_ShippingIdOrderByUpdatedAtDesc(shippingId)
                    .orElseThrow(() -> new APIException("Shipment status not found for ID: " + shippingId, HttpStatus.NOT_FOUND));
            ShipmentStatusLogResponse shipmentStatusLogResponse = shipmentStatusLogMapper.toDTO(fetchedShipmentStatusLog);
            if(fetchedShipmentStatusLog.getAssignedBy() != null){
                Manager manager = managerService.findManagerByAccountID(fetchedShipmentStatusLog.getAssignedBy());
                if(manager != null){
                    shipmentStatusLogResponse.setAssignedByManagerName(manager.getManagerName());
                }
            }
            return shipmentStatusLogResponse;
        }
    
        @Transactional(propagation = Propagation.REQUIRED)
        public HttpStatus operatorSave(ShipmentStatusLogRequest shipmentStatusLogRequest, AuthDetails authDetails) {
            if (shipmentStatusLogRequest == null || authDetails == null) {
                throw new APIException("Payload request and user context are required for updating status tracking states", HttpStatus.BAD_REQUEST);
            }

            Shipment getShipment = shipmentService.internalFetchService(shipmentStatusLogRequest.getShippingId());
            Operator getOperator = operatorService.internalFetchService(shipmentStatusLogRequest.getOperatorId());

            if (getShipment == null) {
                throw new APIException("Shipment not found for operator save: " + shipmentStatusLogRequest.getShippingId(), HttpStatus.NOT_FOUND);
            }
            if (getOperator == null) {
                throw new APIException("Operator not found for ID: " + shipmentStatusLogRequest.getOperatorId(), HttpStatus.NOT_FOUND);
            }

            ShipmentStatusLog mutatedShipmentStatusLog = shipmentStatusLogMapper.toVO(shipmentStatusLogRequest);
            mutatedShipmentStatusLog.setOperator(getOperator);
            mutatedShipmentStatusLog.setShipment(getShipment);
            mutatedShipmentStatusLog.setCurrentLocation(shipmentStatusLogRequest.getCurrentLocation());
            mutatedShipmentStatusLog.setCurrentLatitude(shipmentStatusLogRequest.getCurrentLatitude());
            mutatedShipmentStatusLog.setCurrentLongitude(shipmentStatusLogRequest.getCurrentLongitude());
            mutatedShipmentStatusLog.setDriver(null);
            mutatedShipmentStatusLog.setVehicle(null);
            mutatedShipmentStatusLog.setUpdatedAt(LocalDateTime.now());
            mutatedShipmentStatusLog.setUpdatedBy(authDetails.getAccount().getAccountId());
            mutatedShipmentStatusLog.setAssignedBy(authDetails.getAccount().getAccountId());

            statusLogRepo.save(mutatedShipmentStatusLog);
            return HttpStatus.OK;
        }
    
        @Transactional(propagation = Propagation.REQUIRED)
        public ShipmentStatusLogResponse dependentSave(ShipmentStatusLogRequest shipmentStatusLogRequest, AuthDetails authDetails) {
            if (shipmentStatusLogRequest == null || authDetails == null) {
                throw new APIException("Payload request and user context are required for updating status tracking states", HttpStatus.BAD_REQUEST);
            }
    
            Shipment getShipment = shipmentService.internalFetchService(shipmentStatusLogRequest.getShippingId());
            Operator getOperator = operatorService.internalFetchService(shipmentStatusLogRequest.getOperatorId());
            Driver getDriver = driverService.internalFetchService(shipmentStatusLogRequest.getDriverId());
            Vehicle getVehicle = vehicleService.internalFetchService(shipmentStatusLogRequest.getVehicleId());
    
            if (getShipment == null) {
                throw new APIException("Shipment not found for dependents save: " + shipmentStatusLogRequest.getOperatorId(), HttpStatus.NOT_FOUND);
            }
            if (getOperator == null) {
                throw new APIException("Operator not found for ID: " + shipmentStatusLogRequest.getOperatorId(), HttpStatus.NOT_FOUND);
            }
            if(getDriver == null || getDriver.getDriverId() == null){
                throw new APIException("Driver not found for ID: " + shipmentStatusLogRequest.getDriverId(), HttpStatus.NOT_FOUND);
            }
            if (getVehicle == null || getVehicle.getVehicleId() == null) {
                throw new APIException("Vehicle not found for ID: " + shipmentStatusLogRequest.getVehicleId(), HttpStatus.NOT_FOUND);
            }
    
            ShipmentStatusLog mutatedShipmentStatusLog = shipmentStatusLogMapper.toVO(shipmentStatusLogRequest);
            mutatedShipmentStatusLog.setOperator(getOperator);
            mutatedShipmentStatusLog.setCurrentLocation(shipmentStatusLogRequest.getCurrentLocation());
            mutatedShipmentStatusLog.setCurrentLatitude(shipmentStatusLogRequest.getCurrentLatitude());
            mutatedShipmentStatusLog.setCurrentLongitude(shipmentStatusLogRequest.getCurrentLongitude());
            mutatedShipmentStatusLog.setShipment(getShipment);
            mutatedShipmentStatusLog.setDriver(getDriver);
            mutatedShipmentStatusLog.setVehicle(getVehicle);
            mutatedShipmentStatusLog.setUpdatedAt(LocalDateTime.now());
            mutatedShipmentStatusLog.setUpdatedBy(authDetails.getAccount().getAccountId());
            mutatedShipmentStatusLog.setAssignedBy(authDetails.getAccount().getAccountId());
    
            ShipmentStatusLog updatedShipmentStatus = statusLogRepo.save(mutatedShipmentStatusLog);
            return shipmentStatusLogMapper.toDTO(updatedShipmentStatus);
        }
    
        @Transactional(propagation = Propagation.REQUIRED)
        public ShipmentStatusLogResponse statusSave(ShipmentStatusLogRequest shipmentStatusLogRequest, AuthDetails authDetails) {
            if (shipmentStatusLogRequest == null || authDetails == null) {
                throw new APIException("Payload request and user context are required for updating status tracking states", HttpStatus.BAD_REQUEST);
            }
    
            Shipment getShipment = shipmentService.internalFetchService(shipmentStatusLogRequest.getShippingId());
            Operator getOperator = operatorService.internalFetchService(shipmentStatusLogRequest.getOperatorId());
            Driver getDriver = driverService.internalFetchService(shipmentStatusLogRequest.getDriverId());
            Vehicle getVehicle = vehicleService.internalFetchService(shipmentStatusLogRequest.getVehicleId());
    
            if (getShipment == null) {
                throw new APIException("Shipment not found for status save: " + shipmentStatusLogRequest.getOperatorId(), HttpStatus.NOT_FOUND);
            }
            if (getOperator == null) {
                throw new APIException("Operator not found for ID: " + shipmentStatusLogRequest.getOperatorId(), HttpStatus.NOT_FOUND);
            }
            if(getDriver == null || getDriver.getDriverId() == null){
                throw new APIException("Driver not found for ID: " + shipmentStatusLogRequest.getDriverId(), HttpStatus.NOT_FOUND);
            }
            if (getVehicle == null || getVehicle.getVehicleId() == null) {
                throw new APIException("Vehicle not found for ID: " + shipmentStatusLogRequest.getVehicleId(), HttpStatus.NOT_FOUND);
            }
    
            ShipmentStatusLog mutatedShipmentStatusLog = shipmentStatusLogMapper.toVO(shipmentStatusLogRequest);

            if(shipmentStatusLogRequest.getShippingStatus().equals("DELIVERED")){

                if (getShipment.getCurrentLatitude() == null ||
                        getShipment.getCurrentLongitude() == null) {

                    throw new APIException(
                            "Shipment destination coordinates are missing.",
                            HttpStatus.BAD_REQUEST);
                }

                double distance = calculateDistanceInMeters(
                        getShipment.getCurrentLatitude().doubleValue(),
                        getShipment.getCurrentLongitude().doubleValue(),
                        mutatedShipmentStatusLog.getCurrentLatitude().doubleValue(),
                        mutatedShipmentStatusLog.getCurrentLongitude().doubleValue()
                );

                if (distance > ARRIVAL_RADIUS_METERS) {
                    throw new APIException(
                            "Driver has not reached the destination yet.",
                            HttpStatus.BAD_REQUEST);
                }
            }

            mutatedShipmentStatusLog.setOperator(getOperator);
            mutatedShipmentStatusLog.setShipment(getShipment);
            mutatedShipmentStatusLog.setCurrentLocation(shipmentStatusLogRequest.getCurrentLocation());
            mutatedShipmentStatusLog.setCurrentLatitude(shipmentStatusLogRequest.getCurrentLatitude());
            mutatedShipmentStatusLog.setCurrentLongitude(shipmentStatusLogRequest.getCurrentLongitude());
            mutatedShipmentStatusLog.setDriver(getDriver);
            mutatedShipmentStatusLog.setVehicle(getVehicle);
            mutatedShipmentStatusLog.setUpdatedAt(LocalDateTime.now());
            mutatedShipmentStatusLog.setUpdatedBy(authDetails.getAccount().getAccountId());
            mutatedShipmentStatusLog.setAssignedBy(null);
    
            ShipmentStatusLog updatedShipmentStatus = statusLogRepo.save(mutatedShipmentStatusLog);
            return shipmentStatusLogMapper.toDTO(updatedShipmentStatus);
        }

        private double calculateDistanceInMeters(
                double latitude1,
                double longitude1,
                double latitude2,
                double longitude2) {

            final double earthRadiusMeters = 6371000.0;

            double latitudeDifference =
                    Math.toRadians(latitude2 - latitude1);

            double longitudeDifference =
                    Math.toRadians(longitude2 - longitude1);

            double a =
                    Math.sin(latitudeDifference / 2) *
                            Math.sin(latitudeDifference / 2)
                            +
                            Math.cos(Math.toRadians(latitude1)) *
                                    Math.cos(Math.toRadians(latitude2)) *
                                    Math.sin(longitudeDifference / 2) *
                                    Math.sin(longitudeDifference / 2);

            double c =
                    2 * Math.atan2(
                            Math.sqrt(a),
                            Math.sqrt(1 - a)
                    );

            return earthRadiusMeters * c;
        }
    
        /**
         * Every shipment this driver is currently the assigned driver on
         * (based on each shipment's latest log row), excluding DELIVERED ones.
         * driverAccountId must come from AuthDetails — never trust a client-supplied id for "show me my own shipments".
         */
        @Transactional(readOnly = true)
        public List<Integer> fetchActiveShippingIdsByDriverAccountId(Integer driverAccountId) {
            if (driverAccountId == null) {
                throw new APIException("Driver account context is required", HttpStatus.BAD_REQUEST);
            }
            return statusLogRepo.findActiveShippingIdsByDriverAccountId(driverAccountId);
        }
    }