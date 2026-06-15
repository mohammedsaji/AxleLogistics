package com.app.projectlogistics.Service;

import com.app.projectlogistics.DataTransferObject.Composite.ShipmentSaveDTO;
import com.app.projectlogistics.DataTransferObject.MessageDTO.ResponseMessageDTO;
import com.app.projectlogistics.DataTransferObject.Shipment.RQTShipmentDTO;
import com.app.projectlogistics.DataTransferObject.Shipment.RSPShipmentDTO;
import com.app.projectlogistics.Repository.*;
import com.app.projectlogistics.UserDetailServices.CustomizedUserDetails;
import com.app.projectlogistics.ValueObject.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ShipmentService {

    private final ShipmentRepo shipmentRepo;
    private final CustomerService customerService;
    private final CargoService cargoService;
    private final OperatorService operatorService;
    private final DriverService driverService;
    private final VehicleService vehicleService;
    private final ShipmentStatusService shipmentStatusService;

    public ShipmentService(ShipmentRepo shipmentRepo,
                           CustomerService customerService,
                           CargoService cargoService,
                           OperatorService operatorService,
                           DriverService driverService,
                           VehicleService vehicleService,
                           ShipmentStatusService shipmentStatusService) {
        this.shipmentRepo = shipmentRepo;
        this.customerService = customerService;
        this.cargoService = cargoService;
        this.operatorService = operatorService;
        this.driverService = driverService;
        this.vehicleService = vehicleService;
        this.shipmentStatusService = shipmentStatusService;
    }

    public RSPShipmentDTO fetchShipment(Integer shipmentId){
        ShipmentVO fetchedShipmentVO = shipmentRepo.findById(shipmentId).orElse(new ShipmentVO());
        return voToDTOConverter(fetchedShipmentVO);
    }

    public ResponseMessageDTO fetchAllShipment(int pageNo) {
        if (pageNo < 1) {
            pageNo = 1;
        }
        int elementCount = 25;
        Pageable pageable = PageRequest.of(pageNo-1, elementCount,Sort.by("shippingId").descending());
        Page<ShipmentVO> page = shipmentRepo.findAll(pageable);
        List<ShipmentVO> shipmentVOList = page.getContent();
        List<RSPShipmentDTO> rspShipmentDTOList = new ArrayList<>();

        for(ShipmentVO shipmentVO:shipmentVOList){
            rspShipmentDTOList.add(voToDTOConverter(shipmentVO));
        }

        ResponseMessageDTO responseMessageDTO = new ResponseMessageDTO();
        responseMessageDTO.setStatusCode(200);
        responseMessageDTO.setValue("ShipmentList", rspShipmentDTOList);
        responseMessageDTO.setValue("TotalPages", page.getTotalPages());
        responseMessageDTO.setValue("TotalElements", page.getTotalElements());

        return responseMessageDTO;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public RSPShipmentDTO saveShipment(ShipmentSaveDTO shipmentSaveDTO, CustomizedUserDetails userDetails){
        if (shipmentSaveDTO == null || shipmentSaveDTO.getRqtShipmentDTO() == null || userDetails == null) {
            throw new IllegalArgumentException("Required shipment save parameters or user metadata are missing");
        }
        Map<String,Object> masterVOS = fetchMasterVOS(shipmentSaveDTO,userDetails);
        ShipmentVO savingShipmentVO = dtoToVOConverter("Save",shipmentSaveDTO.getRqtShipmentDTO(),masterVOS);
        ShipmentVO savedShipmentVO = shipmentRepo.save(savingShipmentVO);

        return voToDTOConverter(savedShipmentVO);
    }

    public RSPShipmentDTO updateShipment(ShipmentSaveDTO shipmentSaveDTO, CustomizedUserDetails userDetails){
        if (shipmentSaveDTO == null || shipmentSaveDTO.getRqtShipmentDTO() == null || userDetails == null) {
            throw new IllegalArgumentException("Required shipment update parameters or user metadata are missing");
        }
        Map<String, Object> masterVOS = fetchMasterVOS(shipmentSaveDTO,userDetails);
        ShipmentVO mutatedShipmentVO = dtoToVOConverter("Update", shipmentSaveDTO.getRqtShipmentDTO(), masterVOS);
        ShipmentVO updatedShipmentVO = shipmentRepo.save(mutatedShipmentVO);

        return voToDTOConverter(updatedShipmentVO);
    }

    public RSPShipmentDTO voToDTOConverter(ShipmentVO shipmentVO){
        if (shipmentVO == null) {
            return new RSPShipmentDTO();
        }
        RSPShipmentDTO rspShipmentDTO = new RSPShipmentDTO();
        rspShipmentDTO.setShippingId(shipmentVO.getShippingId());
        rspShipmentDTO.setDeliveryDate(shipmentVO.getDeliveryDate());
        rspShipmentDTO.setShippingFrom(shipmentVO.getShippingFrom());
        rspShipmentDTO.setShippingTo(shipmentVO.getShippingTo());
        rspShipmentDTO.setCreatedAt(shipmentVO.getCreatedAt());
        rspShipmentDTO.setUpdatedAt(shipmentVO.getUpdatedAt());
        rspShipmentDTO.setUpdatedBy(shipmentVO.getUpdatedBy());

        return rspShipmentDTO;
    }

    public ShipmentVO dtoToVOConverter(String action, RQTShipmentDTO rqtShipmentDTO, Map<String,Object> masterVOS){
        if (action == null || rqtShipmentDTO == null || masterVOS == null) {
            return new ShipmentVO();
        }
        CustomerVO customerVO = (CustomerVO) masterVOS.get("Customer");
        CargoVO cargoVO = (CargoVO) masterVOS.get("Cargo");
        ShipmentStatusVO shipmentStatusVO = (ShipmentStatusVO) masterVOS.get("ShipmentStatus");

        ShipmentVO shipmentVO = new ShipmentVO();
        if(action.equalsIgnoreCase("Update")){
            shipmentVO.setShippingId(rqtShipmentDTO.getShippingId());
        }
        shipmentVO.setShippingFrom(rqtShipmentDTO.getShippingFrom());
        shipmentVO.setShippingTo(rqtShipmentDTO.getShippingTo());
        shipmentVO.setDeliveryDate(rqtShipmentDTO.getDeliveryDate());
        shipmentVO.setShippingForUserVO(customerVO);
        shipmentVO.setShippingCargoInfoVO(cargoVO);
        shipmentVO.setShippingStatusInfoVO(shipmentStatusVO);
        if(action.equalsIgnoreCase("Save")){
            shipmentVO.setCreatedAt(LocalDateTime.now());
        }else{
            shipmentVO.setCreatedAt(rqtShipmentDTO.getCreatedAt());
        }
        shipmentVO.setUpdatedAt(LocalDateTime.now());
        shipmentVO.setUpdatedBy(rqtShipmentDTO.getUpdatedBy());
        return shipmentVO;
    }

    @Transactional (propagation = Propagation.REQUIRED)
    public Map<String,Object> fetchMasterVOS(ShipmentSaveDTO shipmentSaveDTO, CustomizedUserDetails userDetails){
        Map<String,Object> masterVOS = new HashMap<>();
        if (shipmentSaveDTO == null || userDetails == null ||
                shipmentSaveDTO.getRqtOperatorDTO() == null ||
                shipmentSaveDTO.getRqtDriverDTO() == null ||
                shipmentSaveDTO.getRqtVehicleDTO() == null) {
            return masterVOS;
        }

        CustomerVO getCustomerVO = customerService.saveCustomer(shipmentSaveDTO.getRqtCustomerDTO());
        CargoVO getCargoVO = cargoService.saveCargo(shipmentSaveDTO.getRqtCargoDTO());
        OperatorVO getOperatorVO = operatorService.internalFetchService(shipmentSaveDTO.getRqtOperatorDTO().getOperatorId());
        DriverVO getDriverVO = driverService.internalFetchService(shipmentSaveDTO.getRqtDriverDTO().getDriverId());
        VehicleVO getVehicleVO = vehicleService.internalFetchService(shipmentSaveDTO.getRqtVehicleDTO().getVehicleId());
        ShipmentStatusVO getShipmentStatusVO = shipmentStatusService.saveShipmentStatus(
                getCargoVO,
                getOperatorVO,
                getDriverVO,
                getVehicleVO,
                userDetails,
                shipmentSaveDTO.getRqtShipmentStatusDTO()
        );
        masterVOS.put("Customer",getCustomerVO);
        masterVOS.put("Cargo", getCargoVO);
        masterVOS.put("ShipmentStatus",getShipmentStatusVO);

        return masterVOS;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public ShipmentVO internalFetchService(Integer shipmentStatusId){

        ShipmentVO fetchedShipmentVO = shipmentRepo.findByShipmentStatusVO_ShippingStatusId(shipmentStatusId);
        return fetchedShipmentVO;
    }


    @Transactional(readOnly = true)
    public RSPShipmentDTO fetchByShipmentId(Integer shipmentId) {
        if (shipmentId == null || shipmentId <= 0) {
            return null;
        }
        ShipmentVO fetchShipmentVO = shipmentRepo.findById(shipmentId).orElse(null);

        if (fetchShipmentVO != null) {
            return voToDTOConverter(fetchShipmentVO);
        }

        return null;
    }

}
