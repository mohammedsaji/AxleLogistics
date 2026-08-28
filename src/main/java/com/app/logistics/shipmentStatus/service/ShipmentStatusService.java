package com.app.logistics.shipmentStatus.service;

import com.app.logistics.cargo.entity.Cargo;
import com.app.logistics.cargo.service.CargoService;
import com.app.logistics.driver.entity.Driver;
import com.app.logistics.driver.service.DriverService;
import com.app.logistics.dto.ShipmentStatus.RQTShipmentStatusDTO;
import com.app.logistics.dto.ShipmentStatus.RSPShipmentStatusDTO;
import com.app.logistics.operator.entity.Operator;
import com.app.logistics.operator.service.OperatorService;
import com.app.logistics.shipmentStatus.entity.ShipmentStatus;
import com.app.logistics.shipmentStatus.repo.StatusRepo;
import com.app.logistics.authUtils.CustomizedUserDetails;
import com.app.logistics.shipmentStatusLog.service.ShipmentStatusLogService;
import com.app.logistics.vehicle.entity.Vehicle;
import com.app.logistics.vehicle.service.VehicleService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ShipmentStatusService {

    private final StatusRepo statusRepo;
    private final ShipmentStatusLogService shipmentStatusLogService;
    private final CargoService cargoService;
    private final OperatorService operatorService;
    private final DriverService driverService;
    private final VehicleService vehicleService;

    public ShipmentStatusService(StatusRepo statusRepo,
                                 @Lazy ShipmentStatusLogService shipmentStatusLogService,
                                 CargoService cargoService,
                                 OperatorService operatorService,
                                 DriverService driverService,
                                 VehicleService vehicleService){
        this.statusRepo = statusRepo;
        this.shipmentStatusLogService = shipmentStatusLogService;
        this.cargoService = cargoService;
        this.operatorService = operatorService;
        this.driverService = driverService;
        this.vehicleService = vehicleService;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public ShipmentStatus saveShipmentStatus(Cargo cargo,
                                             Operator operator,
                                             Driver driver,
                                             Vehicle vehicleVO,
                                             CustomizedUserDetails userDetails,
                                             RQTShipmentStatusDTO rqtShipmentStatusDTO){
        if (rqtShipmentStatusDTO == null || userDetails == null) {
            throw new IllegalArgumentException("Payload request and user context are required for creating status tracking states");
        }
        ShipmentStatus savingShipmentStatus = dtoToVOConverter("Save",rqtShipmentStatusDTO, operator, driver,vehicleVO,userDetails);
        ShipmentStatus savedShipmentStatus = statusRepo.save(savingShipmentStatus);
        shipmentStatusLogService.saveStatusLog(savedShipmentStatus);
        return savedShipmentStatus;
    }

    public RSPShipmentStatusDTO fetchStatus(RQTShipmentStatusDTO rqtShipmentStatusDTO) {
        if (rqtShipmentStatusDTO == null || rqtShipmentStatusDTO.getShippingStatusId() == null) {
            return new RSPShipmentStatusDTO();
        }
        ShipmentStatus fetchedShipmentStatus = statusRepo.findById(rqtShipmentStatusDTO.getShippingStatusId()).orElse(new ShipmentStatus());
        return voTODTOConverter(fetchedShipmentStatus);
    }

    public ShipmentStatus dtoToVOConverter(String action,
                                           RQTShipmentStatusDTO rqtShipmentStatusDTO,
                                           Operator operator,
                                           Driver driver,
                                           Vehicle vehicleVO,
                                           CustomizedUserDetails userDetails){
        if (action == null || rqtShipmentStatusDTO == null || userDetails == null) {
            return new ShipmentStatus();
        }
        ShipmentStatus shipmentStatus = new ShipmentStatus();
        if(action.equalsIgnoreCase("Update")){
            shipmentStatus.setShippingStatusId(rqtShipmentStatusDTO.getShippingStatusId());
        }
        shipmentStatus.setShippingStatus(rqtShipmentStatusDTO.getShippingStatus());
        shipmentStatus.setCurrentLocation(rqtShipmentStatusDTO.getCurrentLocation());
        shipmentStatus.setShippingOperatorVO(operator);
        shipmentStatus.setShippingDriverVO(driver);
        shipmentStatus.setShippingVehicleVO(vehicleVO);
        shipmentStatus.setUpdatedAt(LocalDateTime.now());
        shipmentStatus.setUpdatedby(userDetails.getEmployeeId());

        return shipmentStatus;
    }

    public RSPShipmentStatusDTO voTODTOConverter(ShipmentStatus shipmentStatus){
        if (shipmentStatus == null) {
            return new RSPShipmentStatusDTO();
        }
        RSPShipmentStatusDTO rspShipmentStatusDTO = new RSPShipmentStatusDTO();
        rspShipmentStatusDTO.setShippingStatusId(shipmentStatus.getShippingStatusId());
        rspShipmentStatusDTO.setShippingStatus(shipmentStatus.getShippingStatus());
        rspShipmentStatusDTO.setCurrentLocation(shipmentStatus.getCurrentLocation());
        if (shipmentStatus.getShippingOperatorVO() != null) {
            rspShipmentStatusDTO.setOperatorId(shipmentStatus.getShippingOperatorVO().getOperatorId());
        }
        if (shipmentStatus.getShippingDriverVO() != null) {
            rspShipmentStatusDTO.setDriverId(shipmentStatus.getShippingDriverVO().getDriverId());
        }
        if (shipmentStatus.getShippingVehicleVO() != null) {
            rspShipmentStatusDTO.setVehicleId(shipmentStatus.getShippingVehicleVO().getVehicleId());
        }
        rspShipmentStatusDTO.setUpdatedAt(shipmentStatus.getUpdatedAt());
        rspShipmentStatusDTO.setUpdatedby(shipmentStatus.getUpdatedby());

        return rspShipmentStatusDTO;
    }

    public RSPShipmentStatusDTO updateStatus(RQTShipmentStatusDTO rqtShipmentStatusDTO, CustomizedUserDetails userDetails) {
        if (rqtShipmentStatusDTO == null) {
            return new RSPShipmentStatusDTO();
        }
        Operator getOperator = operatorService.internalFetchService(rqtShipmentStatusDTO.getOperatorId());
        Driver getDriver = driverService.internalFetchService(rqtShipmentStatusDTO.getDriverId());
        Vehicle getVehicleVO = vehicleService.internalFetchService(rqtShipmentStatusDTO.getVehicleId());
        ShipmentStatus mutatedShipmentStatus = dtoToVOConverter("Update", rqtShipmentStatusDTO, getOperator, getDriver, getVehicleVO, userDetails);
        ShipmentStatus updatedShipmentStatus = statusRepo.save(mutatedShipmentStatus);
        shipmentStatusLogService.saveStatusLog(updatedShipmentStatus);
        return voTODTOConverter(updatedShipmentStatus);
    }
}
