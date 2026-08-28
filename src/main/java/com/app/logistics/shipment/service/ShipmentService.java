package com.app.logistics.shipment.service;

import com.app.logistics.cargo.entity.Cargo;
import com.app.logistics.cargo.service.CargoService;
import com.app.logistics.customer.entity.Customer;
import com.app.logistics.customer.service.CustomerService;
import com.app.logistics.driver.entity.Driver;
import com.app.logistics.driver.service.DriverService;
import com.app.logistics.dto.Composite.ShipmentSaveDTO;
import com.app.logistics.dto.MessageDTO.ResponseMessageDTO;
import com.app.logistics.dto.Shipment.RQTShipmentDTO;
import com.app.logistics.dto.Shipment.RSPShipmentDTO;
import com.app.logistics.operator.entity.Operator;
import com.app.logistics.operator.service.OperatorService;
import com.app.logistics.authUtils.CustomizedUserDetails;
import com.app.logistics.shipment.entity.Shipment;
import com.app.logistics.shipment.repo.ShipmentRepo;
import com.app.logistics.shipmentStatus.entity.ShipmentStatus;
import com.app.logistics.shipmentStatus.service.ShipmentStatusService;
import com.app.logistics.vehicle.entity.Vehicle;
import com.app.logistics.vehicle.service.VehicleService;
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
        Shipment fetchedShipment = shipmentRepo.findById(shipmentId).orElse(new Shipment());
        return voToDTOConverter(fetchedShipment);
    }

    public ResponseMessageDTO fetchAllShipment(int pageNo) {
        if (pageNo < 1) {
            pageNo = 1;
        }
        int elementCount = 25;
        Pageable pageable = PageRequest.of(pageNo-1, elementCount,Sort.by("shippingId").descending());
        Page<Shipment> page = shipmentRepo.findAll(pageable);
        List<Shipment> shipmentList = page.getContent();
        List<RSPShipmentDTO> rspShipmentDTOList = new ArrayList<>();

        for(Shipment shipment : shipmentList){
            rspShipmentDTOList.add(voToDTOConverter(shipment));
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
        Shipment savingShipment = dtoToVOConverter("Save",shipmentSaveDTO.getRqtShipmentDTO(),masterVOS);
        Shipment savedShipment = shipmentRepo.save(savingShipment);

        return voToDTOConverter(savedShipment);
    }

    public RSPShipmentDTO updateShipment(ShipmentSaveDTO shipmentSaveDTO, CustomizedUserDetails userDetails){
        if (shipmentSaveDTO == null || shipmentSaveDTO.getRqtShipmentDTO() == null || userDetails == null) {
            throw new IllegalArgumentException("Required shipment update parameters or user metadata are missing");
        }
        Map<String, Object> masterVOS = fetchMasterVOS(shipmentSaveDTO,userDetails);
        Shipment mutatedShipment = dtoToVOConverter("Update", shipmentSaveDTO.getRqtShipmentDTO(), masterVOS);
        Shipment updatedShipment = shipmentRepo.save(mutatedShipment);

        return voToDTOConverter(updatedShipment);
    }

    public RSPShipmentDTO voToDTOConverter(Shipment shipment){
        if (shipment == null) {
            return new RSPShipmentDTO();
        }
        RSPShipmentDTO rspShipmentDTO = new RSPShipmentDTO();
        rspShipmentDTO.setShippingId(shipment.getShippingId());
        rspShipmentDTO.setDeliveryDate(shipment.getDeliveryDate());
        rspShipmentDTO.setShippingFrom(shipment.getShippingFrom());
        rspShipmentDTO.setShippingTo(shipment.getShippingTo());
        rspShipmentDTO.setCreatedAt(shipment.getCreatedAt());
        rspShipmentDTO.setUpdatedAt(shipment.getUpdatedAt());
        rspShipmentDTO.setUpdatedBy(shipment.getUpdatedBy());

        return rspShipmentDTO;
    }

    public Shipment dtoToVOConverter(String action, RQTShipmentDTO rqtShipmentDTO, Map<String,Object> masterVOS){
        if (action == null || rqtShipmentDTO == null || masterVOS == null) {
            return new Shipment();
        }
        Customer customer = (Customer) masterVOS.get("Customer");
        Cargo cargo = (Cargo) masterVOS.get("Cargo");
        ShipmentStatus shipmentStatus = (ShipmentStatus) masterVOS.get("ShipmentStatus");

        Shipment shipment = new Shipment();
        if(action.equalsIgnoreCase("Update")){
            shipment.setShippingId(rqtShipmentDTO.getShippingId());
        }
        shipment.setShippingFrom(rqtShipmentDTO.getShippingFrom());
        shipment.setShippingTo(rqtShipmentDTO.getShippingTo());
        shipment.setDeliveryDate(rqtShipmentDTO.getDeliveryDate());
        shipment.setShippingForUserVO(customer);
        shipment.setShippingCargoInfoVO(cargo);
        shipment.setShippingStatusInfoVO(shipmentStatus);
        if(action.equalsIgnoreCase("Save")){
            shipment.setCreatedAt(LocalDateTime.now());
        }else{
            shipment.setCreatedAt(rqtShipmentDTO.getCreatedAt());
        }
        shipment.setUpdatedAt(LocalDateTime.now());
        shipment.setUpdatedBy(rqtShipmentDTO.getUpdatedBy());
        return shipment;
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

        Customer getCustomer = customerService.saveCustomer(shipmentSaveDTO.getRqtCustomerDTO());
        Cargo getCargo = cargoService.saveCargo(shipmentSaveDTO.getRqtCargoDTO());
        Operator getOperator = operatorService.internalFetchService(shipmentSaveDTO.getRqtOperatorDTO().getOperatorId());
        Driver getDriver = driverService.internalFetchService(shipmentSaveDTO.getRqtDriverDTO().getDriverId());
        Vehicle getVehicleVO = vehicleService.internalFetchService(shipmentSaveDTO.getRqtVehicleDTO().getVehicleId());
        ShipmentStatus getShipmentStatus = shipmentStatusService.saveShipmentStatus(
                getCargo,
                getOperator,
                getDriver,
                getVehicleVO,
                userDetails,
                shipmentSaveDTO.getRqtShipmentStatusDTO()
        );
        masterVOS.put("Customer", getCustomer);
        masterVOS.put("Cargo", getCargo);
        masterVOS.put("ShipmentStatus", getShipmentStatus);

        return masterVOS;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Shipment internalFetchService(Integer shipmentStatusId){

        Shipment fetchedShipment = shipmentRepo.findByShipmentStatusVO_ShippingStatusId(shipmentStatusId);
        return fetchedShipment;
    }


    @Transactional(readOnly = true)
    public RSPShipmentDTO fetchByShipmentId(Integer shipmentId) {
        if (shipmentId == null || shipmentId <= 0) {
            return null;
        }
        Shipment fetchShipment = shipmentRepo.findById(shipmentId).orElse(null);

        if (fetchShipment != null) {
            return voToDTOConverter(fetchShipment);
        }

        return null;
    }

}
