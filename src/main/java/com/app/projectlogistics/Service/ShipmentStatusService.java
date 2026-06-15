package com.app.projectlogistics.Service;

import com.app.projectlogistics.DataTransferObject.ShipmentStatus.RQTShipmentStatusDTO;
import com.app.projectlogistics.DataTransferObject.ShipmentStatus.RSPShipmentStatusDTO;
import com.app.projectlogistics.Repository.StatusRepo;
import com.app.projectlogistics.UserDetailServices.CustomizedUserDetails;
import com.app.projectlogistics.ValueObject.*;
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
    public ShipmentStatusVO saveShipmentStatus(CargoVO cargoVO,
                                    OperatorVO operatorVO,
                                    DriverVO driverVO,
                                    VehicleVO vehicleVO,
                                    CustomizedUserDetails userDetails,
                                    RQTShipmentStatusDTO rqtShipmentStatusDTO){
        if (rqtShipmentStatusDTO == null || userDetails == null) {
            throw new IllegalArgumentException("Payload request and user context are required for creating status tracking states");
        }
        ShipmentStatusVO savingShipmentStatusVO = dtoToVOConverter("Save",rqtShipmentStatusDTO,operatorVO,driverVO,vehicleVO,userDetails);
        ShipmentStatusVO savedShipmentStatusVO = statusRepo.save(savingShipmentStatusVO);
        shipmentStatusLogService.saveStatusLog(savedShipmentStatusVO);
        return savedShipmentStatusVO;
    }

    public RSPShipmentStatusDTO fetchStatus(RQTShipmentStatusDTO rqtShipmentStatusDTO) {
        if (rqtShipmentStatusDTO == null || rqtShipmentStatusDTO.getShippingStatusId() == null) {
            return new RSPShipmentStatusDTO();
        }
        ShipmentStatusVO fetchedShipmentStatusVO = statusRepo.findById(rqtShipmentStatusDTO.getShippingStatusId()).orElse(new ShipmentStatusVO());
        return voTODTOConverter(fetchedShipmentStatusVO);
    }

    public ShipmentStatusVO dtoToVOConverter(String action,
                                             RQTShipmentStatusDTO rqtShipmentStatusDTO,
                                             OperatorVO operatorVO,
                                             DriverVO driverVO,
                                             VehicleVO vehicleVO,
                                             CustomizedUserDetails userDetails){
        if (action == null || rqtShipmentStatusDTO == null || userDetails == null) {
            return new ShipmentStatusVO();
        }
        ShipmentStatusVO shipmentStatusVO = new ShipmentStatusVO();
        if(action.equalsIgnoreCase("Update")){
            shipmentStatusVO.setShippingStatusId(rqtShipmentStatusDTO.getShippingStatusId());
        }
        shipmentStatusVO.setShippingStatus(rqtShipmentStatusDTO.getShippingStatus());
        shipmentStatusVO.setCurrentLocation(rqtShipmentStatusDTO.getCurrentLocation());
        shipmentStatusVO.setShippingOperatorVO(operatorVO);
        shipmentStatusVO.setShippingDriverVO(driverVO);
        shipmentStatusVO.setShippingVehicleVO(vehicleVO);
        shipmentStatusVO.setUpdatedAt(LocalDateTime.now());
        shipmentStatusVO.setUpdatedby(userDetails.getEmployeeId());

        return shipmentStatusVO;
    }

    public RSPShipmentStatusDTO voTODTOConverter(ShipmentStatusVO shipmentStatusVO){
        if (shipmentStatusVO == null) {
            return new RSPShipmentStatusDTO();
        }
        RSPShipmentStatusDTO rspShipmentStatusDTO = new RSPShipmentStatusDTO();
        rspShipmentStatusDTO.setShippingStatusId(shipmentStatusVO.getShippingStatusId());
        rspShipmentStatusDTO.setShippingStatus(shipmentStatusVO.getShippingStatus());
        rspShipmentStatusDTO.setCurrentLocation(shipmentStatusVO.getCurrentLocation());
        if (shipmentStatusVO.getShippingOperatorVO() != null) {
            rspShipmentStatusDTO.setOperatorId(shipmentStatusVO.getShippingOperatorVO().getOperatorId());
        }
        if (shipmentStatusVO.getShippingDriverVO() != null) {
            rspShipmentStatusDTO.setDriverId(shipmentStatusVO.getShippingDriverVO().getDriverId());
        }
        if (shipmentStatusVO.getShippingVehicleVO() != null) {
            rspShipmentStatusDTO.setVehicleId(shipmentStatusVO.getShippingVehicleVO().getVehicleId());
        }
        rspShipmentStatusDTO.setUpdatedAt(shipmentStatusVO.getUpdatedAt());
        rspShipmentStatusDTO.setUpdatedby(shipmentStatusVO.getUpdatedby());

        return rspShipmentStatusDTO;
    }

    public RSPShipmentStatusDTO updateStatus(RQTShipmentStatusDTO rqtShipmentStatusDTO, CustomizedUserDetails userDetails) {
        if (rqtShipmentStatusDTO == null) {
            return new RSPShipmentStatusDTO();
        }
        OperatorVO getOperatorVO = operatorService.internalFetchService(rqtShipmentStatusDTO.getOperatorId());
        DriverVO getDriverVO = driverService.internalFetchService(rqtShipmentStatusDTO.getDriverId());
        VehicleVO getVehicleVO = vehicleService.internalFetchService(rqtShipmentStatusDTO.getVehicleId());
        ShipmentStatusVO mutatedShipmentStatusVO = dtoToVOConverter("Update", rqtShipmentStatusDTO, getOperatorVO, getDriverVO, getVehicleVO, userDetails);
        ShipmentStatusVO updatedShipmentStatusVO = statusRepo.save(mutatedShipmentStatusVO);
        shipmentStatusLogService.saveStatusLog(updatedShipmentStatusVO);
        return voTODTOConverter(updatedShipmentStatusVO);
    }
}
