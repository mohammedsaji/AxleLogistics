package com.app.logistics.shipment.service;

import com.app.logistics.auth.authUtils.AuthDetails;
import com.app.logistics.cargo.entity.Cargo;
import com.app.logistics.cargo.service.CargoService;
import com.app.logistics.common.exception.APIException;
import com.app.logistics.customer.entity.Customer;
import com.app.logistics.customer.service.CustomerService;
import com.app.logistics.driver.entity.Driver;
import com.app.logistics.driver.service.DriverService;
import com.app.logistics.operator.entity.Operator;
import com.app.logistics.operator.service.OperatorService;
import com.app.logistics.shipment.dto.Composite.ShipmentSaveRequest;
import com.app.logistics.shipment.dto.ShipmentRequest;
import com.app.logistics.shipment.dto.ShipmentResponse;
import com.app.logistics.shipment.entity.Shipment;
import com.app.logistics.shipment.repo.ShipmentRepo;
import com.app.logistics.shipment.utils.ShipmentMapper;
import com.app.logistics.shipmentStatus.entity.ShipmentStatus;
import com.app.logistics.shipmentStatus.service.ShipmentStatusService;
import com.app.logistics.vehicle.entity.Vehicle;
import com.app.logistics.vehicle.service.VehicleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
    private final ShipmentStatusService shipmentStatusService;
    private final ShipmentMapper shipmentMapper;

    public ShipmentService(ShipmentRepo shipmentRepo,
                           CustomerService customerService,
                           CargoService cargoService,
                           OperatorService operatorService,
                           DriverService driverService,
                           VehicleService vehicleService,
                           ShipmentStatusService shipmentStatusService,
                           ShipmentMapper shipmentMapper) {
        this.shipmentRepo = shipmentRepo;
        this.customerService = customerService;
        this.cargoService = cargoService;
        this.operatorService = operatorService;
        this.driverService = driverService;
        this.vehicleService = vehicleService;
        this.shipmentStatusService = shipmentStatusService;
        this.shipmentMapper = shipmentMapper;
    }

    @Transactional(readOnly = true)
    public ShipmentResponse fetchShipment(Integer shipmentId) {
        Shipment fetchedShipment = shipmentRepo.findById(shipmentId)
                .orElseThrow(() -> new APIException("Shipment not found for ID: " + shipmentId, HttpStatus.NOT_FOUND));
        return shipmentMapper.toDTO(fetchedShipment);
    }

    @Transactional(readOnly = true)
    public List<ShipmentResponse> fetchAllShipment(int pageNo) {
        if (pageNo < 1) {
            pageNo = 1;
        }
        int elementCount = 25;
        Pageable pageable = PageRequest.of(pageNo - 1, elementCount, Sort.by("shippingId").descending());
        Page<Shipment> page = shipmentRepo.findAll(pageable);
        return page.getContent().stream()
                .map(shipmentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public ShipmentResponse saveShipment(ShipmentSaveRequest shipmentSaveRequest, AuthDetails userDetails) {
        if (shipmentSaveRequest == null || shipmentSaveRequest.getShipmentRequest() == null || userDetails == null) {
            throw new APIException("Required shipment save parameters or user metadata are missing", HttpStatus.BAD_REQUEST);
        }
        Map<String, Object> masterVOS = fetchMasterVOS(shipmentSaveRequest, userDetails);
        Shipment savingShipment = dtoToVOConverter(shipmentSaveRequest.getShipmentRequest(), masterVOS);
        Shipment savedShipment = shipmentRepo.save(savingShipment);
        return shipmentMapper.toDTO(savedShipment);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public ShipmentResponse updateShipment(ShipmentSaveRequest shipmentSaveRequest, AuthDetails userDetails) {
        if (shipmentSaveRequest == null || shipmentSaveRequest.getShipmentRequest() == null || userDetails == null) {
            throw new APIException("Required shipment update parameters or user metadata are missing", HttpStatus.BAD_REQUEST);
        }
        Map<String, Object> masterVOS = fetchMasterVOS(shipmentSaveRequest, userDetails);
        Shipment mutatedShipment = dtoToVOConverter(shipmentSaveRequest.getShipmentRequest(), masterVOS);
        Shipment updatedShipment = shipmentRepo.save(mutatedShipment);
        return shipmentMapper.toDTO(updatedShipment);
    }

    private Shipment dtoToVOConverter(ShipmentRequest shipmentRequest, Map<String, Object> masterVOS) {
        Customer customer = (Customer) masterVOS.get("Customer");
        Cargo cargo = (Cargo) masterVOS.get("Cargo");
        ShipmentStatus shipmentStatus = (ShipmentStatus) masterVOS.get("ShipmentStatus");

        Shipment shipment = shipmentMapper.toVO(shipmentRequest);
        shipment.setShippingForUserVO(customer);
        shipment.setShippingCargoInfoVO(cargo);
        shipment.setShippingStatusInfoVO(shipmentStatus);
        return shipment;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Map<String, Object> fetchMasterVOS(ShipmentSaveRequest shipmentSaveRequest, AuthDetails userDetails) {
        if (shipmentSaveRequest.getOperatorRequest() == null ||
                shipmentSaveRequest.getDriverRequest() == null ||
                shipmentSaveRequest.getVehicleRequest() == null) {
            throw new APIException("Operator, driver, and vehicle details are required to create a shipment", HttpStatus.BAD_REQUEST);
        }

        Customer getCustomer = customerService.saveCustomer(shipmentSaveRequest.getCustomerRequest());
        Cargo getCargo = cargoService.saveCargo(shipmentSaveRequest.getCargoRequest());

        Operator getOperator = operatorService.internalFetchService(shipmentSaveRequest.getOperatorRequest().getOperatorId());
        if (getOperator == null) {
            throw new APIException("Operator not found for ID: " + shipmentSaveRequest.getOperatorRequest().getOperatorId(), HttpStatus.NOT_FOUND);
        }

        Driver getDriver = driverService.internalFetchService(shipmentSaveRequest.getDriverRequest().getDriverId());

        Vehicle getVehicleVO = vehicleService.internalFetchService(shipmentSaveRequest.getVehicleRequest().getVehicleId());
        if (getVehicleVO == null || getVehicleVO.getVehicleId() == null) {
            throw new APIException("Vehicle not found for ID: " + shipmentSaveRequest.getVehicleRequest().getVehicleId(), HttpStatus.NOT_FOUND);
        }

        ShipmentStatus getShipmentStatus = shipmentStatusService.saveShipmentStatus(
                getOperator,
                getDriver,
                getVehicleVO,
                getCargo,
                userDetails.getEmployeeId(),
                shipmentSaveRequest.getShipmentStatusRequest()
        );

        Map<String, Object> masterVOS = new HashMap<>();
        masterVOS.put("Customer", getCustomer);
        masterVOS.put("Cargo", getCargo);
        masterVOS.put("ShipmentStatus", getShipmentStatus);
        return masterVOS;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Shipment internalFetchService(Integer shipmentStatusId) {
        return shipmentRepo.findByShipmentStatusVO_ShippingStatusId(shipmentStatusId);
    }

    @Transactional(readOnly = true)
    public ShipmentResponse fetchByShipmentId(Integer shipmentId) {
        Shipment fetchShipment = shipmentRepo.findById(shipmentId)
                .orElseThrow(() -> new APIException("Shipment not found for ID: " + shipmentId, HttpStatus.NOT_FOUND));
        return shipmentMapper.toDTO(fetchShipment);
    }
}