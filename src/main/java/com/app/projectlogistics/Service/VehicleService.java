package com.app.projectlogistics.Service;

import com.app.projectlogistics.DataTransferObject.MessageDTO.ResponseMessageDTO;
import com.app.projectlogistics.DataTransferObject.Vehicle.RQTVehicleDTO;
import com.app.projectlogistics.DataTransferObject.Vehicle.RSPVehicleDTO;
import com.app.projectlogistics.Repository.VehicleRepo;
import com.app.projectlogistics.UserDetailServices.CustomizedUserDetails;
import com.app.projectlogistics.ValueObject.VehicleVO;
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
        VehicleVO fetchedVehicleVO = vehicleRepo.findById(vehicleId).orElse(new VehicleVO());

        return voToDTOConverter(fetchedVehicleVO);
    }

    public VehicleVO dtoToVOConverter(String action,
                                      RQTVehicleDTO rqtVehicleDTO,
                                      CustomizedUserDetails userDetails) {
        if (action == null || rqtVehicleDTO == null || userDetails == null) {
            return new VehicleVO();
        }
        VehicleVO vehicleVO = new VehicleVO();

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

    public RSPVehicleDTO voToDTOConverter(VehicleVO vehicleVO) {
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
    public VehicleVO internalFetchService(Integer vehicleId) {
        if (vehicleId == null) {
            return new VehicleVO();
        }
        VehicleVO vehicleVO = vehicleRepo.findById(vehicleId).orElse(new VehicleVO());
        return vehicleVO;
    }

    public ResponseMessageDTO fetchAllVehicle(Integer operatorId, int pageNo) {
        if (pageNo < 1) {
            pageNo = 1;
        }
        int elementCount = 25;

        Pageable pageable = PageRequest.of(pageNo - 1, elementCount, Sort.by("vehicleId").ascending());

        Page<VehicleVO> page = vehicleRepo.findByOperatorVO_OperatorId(operatorId, pageable);

        List<VehicleVO> vehicleVOList = page.getContent();
        List<RSPVehicleDTO> rspVehicleDTOList = new ArrayList<>();

        for (VehicleVO vehicleVO : vehicleVOList) {
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
        VehicleVO savingVehicleVO = dtoToVOConverter("Save", rqtVehicleDTO, userDetails);

        VehicleVO savedVehicleVO =
                vehicleRepo.save(savingVehicleVO);

        return voToDTOConverter(savedVehicleVO);
    }

    @Transactional (propagation = Propagation.REQUIRED)
    public RSPVehicleDTO updateVehicle(RQTVehicleDTO rqtVehicleDTO,
                                       CustomizedUserDetails userDetails) {

        if (rqtVehicleDTO == null) {
            return new RSPVehicleDTO();
        }
        VehicleVO mutatedVehicleVO = dtoToVOConverter("Update", rqtVehicleDTO, userDetails);

        VehicleVO updatedVehicleVO = vehicleRepo.save(mutatedVehicleVO);

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

        VehicleVO fetchVehicleVO = vehicleRepo.findByVehicleNumber(vehicleNumber);
        if(fetchVehicleVO != null){
            return voToDTOConverter(fetchVehicleVO);
        }
        return null;
    }
}