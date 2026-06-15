package com.app.projectlogistics.Service;

import com.app.projectlogistics.DataTransferObject.MessageDTO.ResponseMessageDTO;
import com.app.projectlogistics.DataTransferObject.Operator.RQTOperatorDTO;
import com.app.projectlogistics.DataTransferObject.Operator.RSPOperatorDTO;
import com.app.projectlogistics.DataTransferObject.Utilities.CarrierOptionDTO;
import com.app.projectlogistics.Enum.CarrierOptionEnum;
import com.app.projectlogistics.Repository.OperatorRepo;
import com.app.projectlogistics.UserDetailServices.CustomizedUserDetails;
import com.app.projectlogistics.ValueObject.ManagerVO;
import com.app.projectlogistics.ValueObject.OperatorVO;
import org.springframework.context.annotation.Lazy;
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
import java.util.Arrays;
import java.util.List;

@Service
public class OperatorService {

    private final OperatorRepo operatorRepo;
    private final ManagerService managerService;

    public OperatorService(OperatorRepo operatorRepo,
                           @Lazy ManagerService managerService) {
        this.operatorRepo = operatorRepo;
        this.managerService = managerService;
    }

    public ResponseMessageDTO fetchAllOperator(String operatorTransportType, int pageNo) {
        if (pageNo < 1) {
            pageNo = 1;
        }
        int elementCount = 25;
        Pageable pageable = PageRequest.of(pageNo - 1, elementCount, Sort.by("operatorId").descending());

        Page<OperatorVO> page = operatorRepo.findByOperatorTransportType(operatorTransportType, pageable);

        List<OperatorVO> operatorVOList = page.getContent();
        List<RSPOperatorDTO> rspOperatorDTOList = new ArrayList<>();

        for (OperatorVO operatorVO : operatorVOList) {
            rspOperatorDTOList.add(voToDTOConverter(operatorVO));
        }
        ResponseMessageDTO responseMessageDTO = new ResponseMessageDTO();
        responseMessageDTO.setStatusCode(200);
        responseMessageDTO.setValue("OperatorList",rspOperatorDTOList);
        responseMessageDTO.setValue("TotalPages",page.getTotalPages());
        responseMessageDTO.setValue("TotalElements",page.getTotalElements());
        return responseMessageDTO;
    }

    public RSPOperatorDTO fetchOperator(Integer operatorId) {
        if (operatorId == null) {
            return new RSPOperatorDTO();
        }
        OperatorVO fetchedOperatorVO = operatorRepo.findById(operatorId).orElse(null);
        return voToDTOConverter(fetchedOperatorVO);
    }

    public ManagerVO identifyActiveManager(OperatorVO operatorVO) {
        if (operatorVO == null || operatorVO.getManagerVOList() == null) {
            return new ManagerVO();
        }
        List<ManagerVO> managerVOList = operatorVO.getManagerVOList();

        ManagerVO activeManagerVO = new ManagerVO();

        for (ManagerVO managerVO : managerVOList) {
            if (managerVO != null && managerVO.getManagerStatus() != null && managerVO.getManagerStatus().equalsIgnoreCase("ACTIVE")) {
                activeManagerVO = managerVO;
            }
        }
        return activeManagerVO;
    }

    public RSPOperatorDTO voToDTOConverter(OperatorVO operatorVO) {
        if (operatorVO == null) {
            return new RSPOperatorDTO();
        }
        RSPOperatorDTO rspOperatorDTO = new RSPOperatorDTO();
        rspOperatorDTO.setOperatorId(operatorVO.getOperatorId());
        rspOperatorDTO.setOperatorName(operatorVO.getOperatorName());
        rspOperatorDTO.setOperatorTransportType(operatorVO.getOperatorTransportType());
        ManagerVO activeManager = identifyActiveManager(operatorVO);
        if (activeManager != null) {
            rspOperatorDTO.setManagerId(activeManager.getManagerId());
        }
        rspOperatorDTO.setCreatedAt(operatorVO.getCreatedAt());
        rspOperatorDTO.setUpdatedAt(operatorVO.getUpdatedAt());
        rspOperatorDTO.setUpdatedBy(operatorVO.getUpdatedBy());
        return rspOperatorDTO;
    }

    public OperatorVO dtoToVOConverter(String action, RQTOperatorDTO rqtOperatorDTO, CustomizedUserDetails userDetails) {
        if (action == null || rqtOperatorDTO == null || userDetails == null) {
            return null;
        }
        OperatorVO operatorVO = new OperatorVO();
        if (action.equalsIgnoreCase("Update")) {
            operatorVO.setOperatorId(rqtOperatorDTO.getOperatorId());
        }
        operatorVO.setOperatorName(rqtOperatorDTO.getOperatorName());
        operatorVO.setOperatorTransportType(rqtOperatorDTO.getOperatorTransportType());
        if (action.equalsIgnoreCase("Save")) {
            operatorVO.setCreatedAt(LocalDateTime.now());
        } else {
            operatorVO.setCreatedAt(rqtOperatorDTO.getCreatedAt());
        }
        operatorVO.setUpdatedAt(LocalDateTime.now());
        operatorVO.setUpdatedBy(userDetails.getEmployeeId());

        return operatorVO;
    }

    @Transactional (propagation = Propagation.REQUIRED)
    public RSPOperatorDTO saveOperator(RQTOperatorDTO rqtOperatorDTO, CustomizedUserDetails userDetails) {
        if (rqtOperatorDTO == null || userDetails == null) {
            throw new IllegalArgumentException("Payload request body and user context cannot be null");
        }
        OperatorVO savingOperatorVO = dtoToVOConverter("Save", rqtOperatorDTO, userDetails);
        OperatorVO savedOperatorVO = operatorRepo.save(savingOperatorVO);
        return voToDTOConverter(savedOperatorVO);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public OperatorVO internalFetchService(Integer operatorId) {
        if (operatorId == null) {
            return null;
        }
        return operatorRepo.findById(operatorId).orElse(null);
    }

    @Transactional (propagation = Propagation.REQUIRED)
    public RSPOperatorDTO updateOperator(RQTOperatorDTO rqtOperatorDTO, CustomizedUserDetails userDetails) {
        if (rqtOperatorDTO == null) {
            return new RSPOperatorDTO();
        }
        OperatorVO mutatedOperatorVO = dtoToVOConverter("Update", rqtOperatorDTO, userDetails);
        OperatorVO updatedOperatorVO = operatorRepo.save(mutatedOperatorVO);
        return voToDTOConverter(updatedOperatorVO);
    }

    @Transactional (propagation = Propagation.REQUIRED)
    public ResponseEntity<String> deleteOperator(Integer operatorId) {
        if (operatorId == null) {
            return ResponseEntity.status(400).body("Operator ID parameter cannot be null");
        }
        operatorRepo.deleteById(operatorId);
        return ResponseEntity.status(200).body("Operator deleted successfully.");
    }

    public CarrierOptionDTO fetchCarrierOption() {

        CarrierOptionDTO carrierOptionDTO = new CarrierOptionDTO();

        for (CarrierOptionEnum carrierOptionEnum : CarrierOptionEnum.values()) {
            carrierOptionDTO.setCarrierOptionEnumList(carrierOptionEnum);
        }

        return carrierOptionDTO;
    }

    @Transactional(readOnly = true)
    public RSPOperatorDTO fetchByOperatorName(String operatorName) {
        if (operatorName == null || operatorName.trim().isEmpty()) {
            return null;
        }

        OperatorVO fetchOperatorVO = operatorRepo.findByOperatorName(operatorName);
        if(fetchOperatorVO != null){
            return voToDTOConverter(fetchOperatorVO);
        }
        return null;
    }
}
