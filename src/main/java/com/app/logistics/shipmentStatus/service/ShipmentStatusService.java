package com.app.logistics.shipmentStatus.service;

import com.app.logistics.auth.authUtils.AuthDetails;
import com.app.logistics.cargo.entity.Cargo;
import com.app.logistics.cargo.service.CargoService;
import com.app.logistics.common.exception.APIException;
import com.app.logistics.driver.entity.Driver;
import com.app.logistics.driver.service.DriverService;
import com.app.logistics.operator.entity.Operator;
import com.app.logistics.operator.service.OperatorService;
import com.app.logistics.shipmentStatus.dto.ShipmentStatusRequest;
import com.app.logistics.shipmentStatus.dto.ShipmentStatusResponse;
import com.app.logistics.shipmentStatus.entity.ShipmentStatus;
import com.app.logistics.shipmentStatus.repo.StatusRepo;
import com.app.logistics.shipmentStatus.utils.ShipmentStatusMapper;
import com.app.logistics.shipmentStatusLog.service.ShipmentStatusLogService;
import com.app.logistics.vehicle.entity.Vehicle;
import com.app.logistics.vehicle.service.VehicleService;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
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
    private final ShipmentStatusMapper shipmentStatusMapper;

    public ShipmentStatusService(StatusRepo statusRepo,
                                 @Lazy ShipmentStatusLogService shipmentStatusLogService,
                                 CargoService cargoService,
                                 OperatorService operatorService,
                                 DriverService driverService,
                                 VehicleService vehicleService,
                                 ShipmentStatusMapper shipmentStatusMapper){
        this.statusRepo = statusRepo;
        this.shipmentStatusLogService = shipmentStatusLogService;
        this.cargoService = cargoService;
        this.operatorService = operatorService;
        this.driverService = driverService;
        this.vehicleService = vehicleService;
        this.shipmentStatusMapper = shipmentStatusMapper;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public ShipmentStatus saveShipmentStatus(Operator operator,
                                             Driver driver,
                                             Vehicle vehicleVO,
                                             Cargo cargo,
                                             Integer updatedBy,
                                             ShipmentStatusRequest shipmentStatusRequest){
        if (shipmentStatusRequest == null || updatedBy == null) {
            throw new APIException("Payload request and user context are required for creating status tracking states", HttpStatus.BAD_REQUEST);
        }
        ShipmentStatus savingShipmentStatus = shipmentStatusMapper.toVO(shipmentStatusRequest);
        savingShipmentStatus.setShippingOperatorVO(operator);
        savingShipmentStatus.setShippingDriverVO(driver);
        savingShipmentStatus.setShippingVehicleVO(vehicleVO);
        savingShipmentStatus.setUpdatedAt(LocalDateTime.now());
        savingShipmentStatus.setUpdatedby(updatedBy);

        ShipmentStatus savedShipmentStatus = statusRepo.save(savingShipmentStatus);
        shipmentStatusLogService.saveStatusLog(savedShipmentStatus, cargo);
        return savedShipmentStatus;
    }

    @Transactional(readOnly = true)
    public ShipmentStatusResponse fetchStatus(Integer shippingStatusId) {
        ShipmentStatus fetchedShipmentStatus = statusRepo.findById(shippingStatusId)
                .orElseThrow(() -> new APIException("Shipment status not found for ID: " + shippingStatusId, HttpStatus.NOT_FOUND));
        return shipmentStatusMapper.toDTO(fetchedShipmentStatus);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public ShipmentStatusResponse updateStatus(ShipmentStatusRequest shipmentStatusRequest, AuthDetails userDetails) {
        if (shipmentStatusRequest == null || userDetails == null) {
            throw new APIException("Payload request and user context are required for updating status tracking states", HttpStatus.BAD_REQUEST);
        }

        Operator getOperator = operatorService.internalFetchService(shipmentStatusRequest.getOperatorId());
        if (getOperator == null) {
            throw new APIException("Operator not found for ID: " + shipmentStatusRequest.getOperatorId(), HttpStatus.NOT_FOUND);
        }

        Driver getDriver = driverService.internalFetchService(shipmentStatusRequest.getDriverId());

        Vehicle getVehicleVO = vehicleService.internalFetchService(shipmentStatusRequest.getVehicleId());
        if (getVehicleVO == null || getVehicleVO.getVehicleId() == null) {
            throw new APIException("Vehicle not found for ID: " + shipmentStatusRequest.getVehicleId(), HttpStatus.NOT_FOUND);
        }

        Cargo getCargo = cargoService.internalFetchService(shipmentStatusRequest.getCargoId());

        ShipmentStatus mutatedShipmentStatus = shipmentStatusMapper.toVO(shipmentStatusRequest);
        mutatedShipmentStatus.setShippingOperatorVO(getOperator);
        mutatedShipmentStatus.setShippingDriverVO(getDriver);
        mutatedShipmentStatus.setShippingVehicleVO(getVehicleVO);
        mutatedShipmentStatus.setUpdatedAt(LocalDateTime.now());
        mutatedShipmentStatus.setUpdatedby(userDetails.getEmployeeId());

        ShipmentStatus updatedShipmentStatus = statusRepo.save(mutatedShipmentStatus);
        shipmentStatusLogService.saveStatusLog(updatedShipmentStatus, getCargo);
        return shipmentStatusMapper.toDTO(updatedShipmentStatus);
    }
}