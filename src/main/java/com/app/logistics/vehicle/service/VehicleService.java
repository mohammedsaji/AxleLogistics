package com.app.logistics.vehicle.service;

import com.app.logistics.dto.MessageDTO.ResponseMessageDTO;
import com.app.logistics.dto.Vehicle.RQTVehicleDTO;
import com.app.logistics.dto.Vehicle.RSPVehicleDTO;
import com.app.logistics.vehicle.repo.VehicleRepo;
import com.app.logistics.authUtils.CustomizedUserDetails;
import com.app.logistics.vehicle.entity.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
public class VehicleService {

    private final VehicleRepo vehicleRepo;

    public VehicleService(VehicleRepo vehicleRepo) {
        this.vehicleRepo = vehicleRepo;
    }

    public RSPVehicleDTO fetchVehicle(Integer vehicleId) {
        if (vehicleId == null) {
            return new RSPVehicleDTO();
        }
        Vehicle fetchedVehicleVO = vehicleRepo.findById(vehicleId).orElse(new Vehicle());

        return voToDTOConverter(fetchedVehicleVO);
    }

    public Vehicle dtoToVOConverter(String action,
                                    RQTVehicleDTO rqtVehicleDTO,
                                    CustomizedUserDetails userDetails) {
        if (action == null || rqtVehicleDTO == null || userDetails == null) {
            return new Vehicle();
        }
        Vehicle vehicleVO = new Vehicle();

        if (action.equalsIgnoreCase("Update")) {
            vehicleVO.setVehicleId(rqtVehicleDTO.getVehicleId());
        }

        vehicleVO.setVehicleType(rqtVehicleDTO.getVehicleType());
        vehicleVO.setVehicleNumber(rqtVehicleDTO.getVehicleNumber());

        if (action.equalsIgnoreCase("Save")) {
            vehicleVO.setCreatedAt(LocalDateTime.now());
        } else {
            vehicleVO.setCreatedAt(rqtVehicleDTO.getCreatedAt());
        }

        vehicleVO.setUpdatedAt(LocalDateTime.now());
        vehicleVO.setUpdatedBy(userDetails.getEmployeeId());

        return vehicleVO;
    }

    public RSPVehicleDTO voToDTOConverter(Vehicle vehicleVO) {
        if (vehicleVO == null) {
            return new RSPVehicleDTO();
        }
        RSPVehicleDTO rspVehicleDTO = new RSPVehicleDTO();

        rspVehicleDTO.setVehicleId(vehicleVO.getVehicleId());
        rspVehicleDTO.setVehicleType(vehicleVO.getVehicleType());
        rspVehicleDTO.setVehicleNumber(vehicleVO.getVehicleNumber());
        if (vehicleVO.getshippingOperatorVO() != null) {
            rspVehicleDTO.setOperatorId(vehicleVO.getshippingOperatorVO().getOperatorId());
        }
        rspVehicleDTO.setCreatedAt(vehicleVO.getCreatedAt());
        rspVehicleDTO.setUpdatedAt(vehicleVO.getUpdatedAt());
        rspVehicleDTO.setUpdatedBy(vehicleVO.getUpdatedBy());

        return rspVehicleDTO;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Vehicle internalFetchService(Integer vehicleId) {
        if (vehicleId == null) {
            return new Vehicle();
        }
        Vehicle vehicleVO = vehicleRepo.findById(vehicleId).orElse(new Vehicle());
        return vehicleVO;
    }

    public ResponseMessageDTO fetchAllVehicle(Integer operatorId, int pageNo) {
        if (pageNo < 1) {
            pageNo = 1;
        }
        int elementCount = 25;

        Pageable pageable = PageRequest.of(pageNo - 1, elementCount, Sort.by("vehicleId").ascending());

        Page<Vehicle> page = vehicleRepo.findByOperatorVO_OperatorId(operatorId, pageable);

        List<Vehicle> vehicleVOList = page.getContent();
        List<RSPVehicleDTO> rspVehicleDTOList = new ArrayList<>();

        for (Vehicle vehicleVO : vehicleVOList) {
            rspVehicleDTOList.add(voToDTOConverter(vehicleVO));
        }


        ResponseMessageDTO responseMessageDTO = new ResponseMessageDTO();
        responseMessageDTO.setStatusCode(200);
        responseMessageDTO.setValue("ShipmentList", rspVehicleDTOList);
        responseMessageDTO.setValue("TotalPages", page.getTotalPages());
        responseMessageDTO.setValue("TotalElements", page.getTotalElements());

        return responseMessageDTO;
    }

    @Transactional (propagation = Propagation.REQUIRED)
    public RSPVehicleDTO saveVehicle(RQTVehicleDTO rqtVehicleDTO,
                                     CustomizedUserDetails userDetails) {
        if (rqtVehicleDTO == null || userDetails == null) {
            throw new IllegalArgumentException("Payload request and user context are required to register a vehicle");
        }
        Vehicle savingVehicleVO = dtoToVOConverter("Save", rqtVehicleDTO, userDetails);

        Vehicle savedVehicleVO =
                vehicleRepo.save(savingVehicleVO);

        return voToDTOConverter(savedVehicleVO);
    }

    @Transactional (propagation = Propagation.REQUIRED)
    public RSPVehicleDTO updateVehicle(RQTVehicleDTO rqtVehicleDTO,
                                       CustomizedUserDetails userDetails) {

        if (rqtVehicleDTO == null) {
            return new RSPVehicleDTO();
        }
        Vehicle mutatedVehicleVO = dtoToVOConverter("Update", rqtVehicleDTO, userDetails);

        Vehicle updatedVehicleVO = vehicleRepo.save(mutatedVehicleVO);

        return voToDTOConverter(updatedVehicleVO);
    }

    @Transactional (propagation = Propagation.REQUIRED)
    public ResponseEntity<String> deleteVehicle(Integer vehicleId) {
        if (vehicleId == null) {
            return ResponseEntity.status(400).body("Vehicle ID parameter cannot be null");
        }
        vehicleRepo.deleteById(vehicleId);
        return ResponseEntity.status(200).body("Vehicle deleted successfully.");
    }

    @Transactional(readOnly = true)
    public RSPVehicleDTO fetchByVehicleNumber(String vehicleNumber) {

        Vehicle fetchVehicleVO = vehicleRepo.findByVehicleNumber(vehicleNumber);
        if(fetchVehicleVO != null){
            return voToDTOConverter(fetchVehicleVO);
        }
        return null;
    }
}