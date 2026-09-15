package com.app.logistics.shipment.service;

import com.app.logistics.auth.authUtils.AuthDetails;
import com.app.logistics.cargo.entity.Cargo;
import com.app.logistics.cargo.service.CargoService;
import com.app.logistics.cargo.utils.CargoMapper;
import com.app.logistics.common.exception.APIException;
import com.app.logistics.customer.entity.Customer;
import com.app.logistics.customer.service.CustomerService;
import com.app.logistics.customer.utils.CustomerMapper;
import com.app.logistics.driver.entity.Driver;
import com.app.logistics.driver.service.DriverService;
import com.app.logistics.operator.entity.Operator;
import com.app.logistics.operator.service.OperatorService;
import com.app.logistics.shipment.dto.Composite.ShipmentFetchResponse;
import com.app.logistics.shipment.dto.Composite.ShipmentSaveRequest;
import com.app.logistics.shipment.dto.Composite.ShipmentUpdate;
import com.app.logistics.shipment.dto.ShipmentRequest;
import com.app.logistics.shipment.dto.ShipmentResponse;
import com.app.logistics.shipment.dto.ShipmentTrackingResponse;
import com.app.logistics.shipment.entity.Shipment;
import com.app.logistics.shipment.repo.ShipmentRepo;
import com.app.logistics.shipment.utils.ShipmentMapper;
import com.app.logistics.shipmentStatusLog.dto.ShipmentStatusLogResponse;
import com.app.logistics.shipmentStatusLog.entity.ShipmentStatusLog;
import com.app.logistics.shipmentStatusLog.service.ShipmentStatusLogService;
import com.app.logistics.vehicle.entity.Vehicle;
import com.app.logistics.vehicle.service.VehicleService;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ShipmentService {

    private final ShipmentRepo shipmentRepo;
    private final CustomerService customerService;
    private final CargoService cargoService;
    private final OperatorService operatorService;
    private final DriverService driverService;
    private final VehicleService vehicleService;
    private final ShipmentStatusLogService shipmentStatusLogService;
    private final ShipmentMapper shipmentMapper;
    private final CargoMapper cargoMapper;
    private final CustomerMapper customerMapper;

    public ShipmentService(ShipmentRepo shipmentRepo,
                           CustomerService customerService,
                           CargoService cargoService,
                           OperatorService operatorService,
                           DriverService driverService,
                           VehicleService vehicleService,
                           @Lazy ShipmentStatusLogService shipmentStatusLogService,
                           ShipmentMapper shipmentMapper,
                           CargoMapper cargoMapper,
                           CustomerMapper customerMapper) {
        this.shipmentRepo = shipmentRepo;
        this.customerService = customerService;
        this.cargoService = cargoService;
        this.operatorService = operatorService;
        this.driverService = driverService;
        this.vehicleService = vehicleService;
        this.shipmentStatusLogService = shipmentStatusLogService;
        this.shipmentMapper = shipmentMapper;
        this.cargoMapper = cargoMapper;
        this.customerMapper = customerMapper;
    }

    /**
     * Fetch a single shipment, enforcing ownership for drivers.
     * ADMIN / FEDERATE-MANAGER can view any shipment.
     * FEDERATE-DRIVER can only view a shipment they are the currently
     * assigned driver on (checked against the latest status log row).
     * Never trust a client-supplied driverId for this check — always
     * resolve identity from authDetails.
     */
    @Transactional(readOnly = true)
    public ShipmentFetchResponse fetchShipment(Integer shipmentId, AuthDetails authDetails) {
        Shipment fetchedShipment = shipmentRepo.findById(shipmentId)
                .orElseThrow(() -> new APIException("Shipment not found for ID: " + shipmentId, HttpStatus.NOT_FOUND));

        if (authDetails != null && isDriverOnly(authDetails)) {
            ShipmentStatusLogResponse latestLog = shipmentStatusLogService.fetchCurrentStatus(shipmentId);
            Integer requestingDriverId = authDetails.getAccount().getAccountId();
            boolean isAssignedDriver =
                    latestLog.getDriverId() != null &&
                            driverService.internalFetchService(latestLog.getDriverId()) != null &&
                            driverService.internalFetchService(latestLog.getDriverId()).getAccount().getAccountId().equals(requestingDriverId);
            if (!isAssignedDriver) {
                throw new APIException("You are not assigned to this shipment", HttpStatus.FORBIDDEN);
            }
        }
        ShipmentFetchResponse shipmentFetchResponse = new ShipmentFetchResponse();
        shipmentFetchResponse.setShipmentResponse(shipmentMapper.toDTO(fetchedShipment));
        if(fetchedShipment.getShippingCargo().getCargoId() != null){
            shipmentFetchResponse.setCargoResponse(cargoMapper.toDTO(cargoService.internalFetchService(fetchedShipment.getShippingCargo().getCargoId())));
        }
        if(fetchedShipment.getShippingForCustomer().getCustomerId() != null){
            shipmentFetchResponse.setCustomerResponse(customerMapper.toDTO(customerService.internalFetchService(fetchedShipment.getShippingForCustomer().getCustomerId())));
        }
        return shipmentFetchResponse;
    }

    private boolean isDriverOnly(AuthDetails authDetails) {
        return authDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("FEDERATE-DRIVER"))
                && authDetails.getAuthorities().stream()
                .noneMatch(a -> a.getAuthority().equals("ADMIN") || a.getAuthority().equals("FEDERATE-MANAGER"));
    }

    @Transactional(readOnly = true)
    public Map<String, Object> fetchAllShipment(int pageNo) {
        if (pageNo < 1) {
            pageNo = 1;
        }
        int elementCount = 1;
        Pageable pageable = PageRequest.of(pageNo - 1, elementCount, Sort.by("shippingId").descending());
        Page<Shipment> page = shipmentRepo.findAll(pageable);

        Map<String, Object> valueMap = new HashMap<>();
        valueMap.put("shipmentList", page.getContent().stream().map(shipmentMapper::toDTO).collect(Collectors.toList()));
        valueMap.put("totalPages", page.getTotalPages());
        return valueMap;
    }

    /**
     * Driver-scoped list: every shipment currently assigned to the
     * requesting driver's own account (identity from authDetails,
     * never from a request param). Excludes DELIVERED shipments.
     * Requires ShipmentStatusLogService to expose this lookup —
     * see fetchActiveByDriverAccountId below.
     */
    @Transactional(readOnly = true)
    public List<ShipmentResponse> fetchMyActiveShipments(AuthDetails authDetails) {
        if (authDetails == null) {
            throw new APIException("User context is required", HttpStatus.BAD_REQUEST);
        }
        Integer driverAccountId = authDetails.getAccount().getAccountId();
        List<Integer> activeShippingIds = shipmentStatusLogService.fetchActiveShippingIdsByDriverAccountId(driverAccountId);

        return shipmentRepo.findAllById(activeShippingIds).stream()
                .map(shipmentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public ShipmentResponse saveShipment(ShipmentSaveRequest shipmentSaveRequest, AuthDetails authDetails) {
        if (shipmentSaveRequest == null || shipmentSaveRequest.getShipmentRequest() == null || authDetails == null) {
            throw new APIException("Required shipment save parameters or user metadata are missing", HttpStatus.BAD_REQUEST);
        }
        Map<String, Object> masterVOS = fetchMasterVOS(shipmentSaveRequest, authDetails);
        Shipment savingShipment = dtoToVOConverter(shipmentSaveRequest.getShipmentRequest(), masterVOS);
        Shipment savedShipment = shipmentRepo.save(savingShipment);
        Map<String, Object> currentLocationDetails = new HashMap<>();
        if(savingShipment.getCurrentLocation() != null && savingShipment.getCurrentLatitude() != null && savingShipment.getCurrentLongitude() != null){
            currentLocationDetails.put("currentLocation",savingShipment.getCurrentLocation());
            currentLocationDetails.put("currentLatitude",savingShipment.getCurrentLatitude());
            currentLocationDetails.put("currentLongitude",savedShipment.getCurrentLongitude());
        }
        // NOTE: accountId, not employeeId — ADMIN accounts have no Employee row
        // and would NPE here (same bug class fixed in ShipmentStatusLogService).
        ShipmentStatusLog savedShipmenStatusLog = shipmentStatusLogService.saveShipmentStatusLog(
                (Operator)masterVOS.get("Operator"),
                (Driver) masterVOS.get("Driver"),
                (Vehicle) masterVOS.get("Vehicle"),
                authDetails.getAccount().getAccountId(),
                currentLocationDetails,
                savedShipment
        );
        if(savedShipmenStatusLog == null || savedShipmenStatusLog.getShippingStatusLogId() == null){
            throw new APIException("Shipment Status Log not saved, Hence shipment save will be revoked.",HttpStatus.BAD_REQUEST);
        }
        return shipmentMapper.toDTO(savedShipment);
    }

    private Shipment dtoToVOConverter(ShipmentRequest shipmentRequest, Map<String, Object> masterVOS) {
        Customer customer = (Customer) masterVOS.get("Customer");
        Cargo cargo = (Cargo) masterVOS.get("Cargo");
        Shipment shipment = shipmentMapper.toVO(shipmentRequest);
        shipment.setShippingForCustomer(customer);
        shipment.setShippingCargo(cargo);
        return shipment;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Map<String, Object> fetchMasterVOS(ShipmentSaveRequest shipmentSaveRequest, AuthDetails authDetails) {
        if (shipmentSaveRequest.getOperatorRequest() == null ||
                shipmentSaveRequest.getDriverRequest() == null ||
                shipmentSaveRequest.getVehicleRequest() == null) {
            throw new APIException("Operator, driver, and vehicle details are required to create a shipment", HttpStatus.BAD_REQUEST);
        }

        Customer getCustomer = customerService.saveCustomer(shipmentSaveRequest.getCustomerRequest(), authDetails.getAccount().getAccountId());
        Cargo getCargo = cargoService.saveCargo(shipmentSaveRequest.getCargoRequest(), authDetails.getAccount().getAccountId());

        Operator getOperator = operatorService.internalFetchService(shipmentSaveRequest.getOperatorRequest().getOperatorId());
        if (getOperator == null) {
            throw new APIException("Operator not found for ID: " + shipmentSaveRequest.getOperatorRequest().getOperatorId(), HttpStatus.NOT_FOUND);
        }

        Driver getDriver = driverService.internalFetchService(shipmentSaveRequest.getDriverRequest().getDriverId());

        Vehicle getVehicle = vehicleService.internalFetchService(shipmentSaveRequest.getVehicleRequest().getVehicleId());
        if (getVehicle == null || getVehicle.getVehicleId() == null) {
            throw new APIException("Vehicle not found for ID: " + shipmentSaveRequest.getVehicleRequest().getVehicleId(), HttpStatus.NOT_FOUND);
        }

        Map<String, Object> masterVOS = new HashMap<>();
        masterVOS.put("Customer", getCustomer);
        masterVOS.put("Cargo", getCargo);
        masterVOS.put("Operator",getOperator);
        masterVOS.put("Driver",getDriver);
        masterVOS.put("Vehicle",getVehicle);
        return masterVOS;
    }

    @Transactional(readOnly = true)
    public ShipmentTrackingResponse trackShipment(Integer shippingId) {

        if (shippingId == null) {
            throw new APIException(
                    "Shipping ID is required",
                    HttpStatus.BAD_REQUEST);
        }

        Shipment shipment = shipmentRepo.findById(shippingId)
                .orElseThrow(() -> new APIException(
                        "Shipment not found for ID: " + shippingId,
                        HttpStatus.NOT_FOUND));

        ShipmentStatusLogResponse latestStatus =
                shipmentStatusLogService.fetchCurrentStatus(shippingId);

        ShipmentTrackingResponse shipmentTrackingResponse = new ShipmentTrackingResponse();

        shipmentTrackingResponse.setShippingId(shipment.getShippingId());
        shipmentTrackingResponse.setShippingFrom(shipment.getShippingFrom());
        shipmentTrackingResponse.setShippingTo(shipment.getShippingTo());
        shipmentTrackingResponse.setDeliveryDate(shipment.getDeliveryDate());

        // Cargo
        Cargo fetchedCargo = cargoService.internalFetchService(shipment.getShippingCargo().getCargoId());
        shipmentTrackingResponse.setCargoName(fetchedCargo.getCargoName());
        shipmentTrackingResponse.setCargoQuantity(fetchedCargo.getCargoQuantity());
        shipmentTrackingResponse.setCargoType(fetchedCargo.getCargoType());

        // Customer
        Customer fetchedCustomer = customerService.internalFetchService(shipment.getShippingForCustomer().getCustomerId());
        shipmentTrackingResponse.setCustomerName(fetchedCustomer.getCustomerName());

        // Latest status log
        shipmentTrackingResponse.setCurrentLocation(latestStatus.getCurrentLocation());
        shipmentTrackingResponse.setShippingStatus(latestStatus.getShippingStatus());

        // Operator
        Operator fetchedOperator = operatorService.internalFetchService(latestStatus.getOperatorId());
        shipmentTrackingResponse.setOperatorName(fetchedOperator.getOperatorName());

        return shipmentTrackingResponse;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public HttpStatus updateShipment(ShipmentUpdate shipmentUpdate, AuthDetails authDetails) {
        if (shipmentUpdate == null || shipmentUpdate.getShipmentRequest() == null || shipmentUpdate.getShipmentRequest().getShippingId() == null || authDetails == null) {
            throw new APIException("Required shipment update parameters or user metadata are missing", HttpStatus.BAD_REQUEST);
        }

        Shipment existingShipment = shipmentRepo.findById(shipmentUpdate.getShipmentRequest().getShippingId())
                .orElseThrow(() -> new APIException("Shipment not found for ID: " + shipmentUpdate.getShipmentRequest().getShippingId(), HttpStatus.NOT_FOUND));

        ShipmentRequest shipmentRequest = shipmentUpdate.getShipmentRequest();
        existingShipment.setCurrentLocation(shipmentRequest.getCurrentLocation());
        existingShipment.setCurrentLatitude(shipmentRequest.getCurrentLatitude());
        existingShipment.setCurrentLongitude(shipmentRequest.getCurrentLongitude());
        existingShipment.setUpdatedBy(authDetails.getAccount().getAccountId());
        Shipment updatedShipment = shipmentRepo.save(existingShipment);

        // Same transaction: the accompanying status log row commits or
        // rolls back together with the shipment edit above.
        return shipmentStatusLogService.operatorSave(shipmentUpdate.getShipmentStatusLogRequest(), authDetails);
    }

    /**
     * Internal lookup by shipment's own primary key. The old version of
     * this method queried by a status FK that no longer exists on
     * SHIPPNG_INFO — every caller was actually passing a shippingId,
     * not a status id, so this now matches what's actually being asked for.
     */
    @Transactional(readOnly = true)
    public Shipment internalFetchService(Integer shippingId) {
        return shipmentRepo.findById(shippingId).orElse(null);
    }
}