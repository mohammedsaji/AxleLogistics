package com.app.logistics.operator.service;

import com.app.logistics.dto.MessageDTO.ResponseMessageDTO;
import com.app.logistics.dto.Operator.RQTOperatorDTO;
import com.app.logistics.dto.Operator.RSPOperatorDTO;
import com.app.logistics.common.dto.OperatorRequest;
import com.app.logistics.Enum.CarrierOptionEnum;
import com.app.logistics.manager.service.ManagerService;
import com.app.logistics.operator.repo.OperatorRepo;
import com.app.logistics.authUtils.CustomizedUserDetails;
import com.app.logistics.manager.entity.Manager;
import com.app.logistics.operator.entity.Operator;
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

        Page<Operator> page = operatorRepo.findByOperatorTransportType(operatorTransportType, pageable);

        List<Operator> operatorList = page.getContent();
        List<RSPOperatorDTO> rspOperatorDTOList = new ArrayList<>();

        for (Operator operator : operatorList) {
            rspOperatorDTOList.add(voToDTOConverter(operator));
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
        Operator fetchedOperator = operatorRepo.findById(operatorId).orElse(null);
        return voToDTOConverter(fetchedOperator);
    }

    public Manager identifyActiveManager(Operator operator) {
        if (operator == null || operator.getManagerVOList() == null) {
            return new Manager();
        }
        List<Manager> managerList = operator.getManagerVOList();

        Manager activeManager = new Manager();

        for (Manager manager : managerList) {
            if (manager != null && manager.getManagerStatus() != null && manager.getManagerStatus().equalsIgnoreCase("ACTIVE")) {
                activeManager = manager;
            }
        }
        return activeManager;
    }

    public RSPOperatorDTO voToDTOConverter(Operator operator) {
        if (operator == null) {
            return new RSPOperatorDTO();
        }
        RSPOperatorDTO rspOperatorDTO = new RSPOperatorDTO();
        rspOperatorDTO.setOperatorId(operator.getOperatorId());
        rspOperatorDTO.setOperatorName(operator.getOperatorName());
        rspOperatorDTO.setOperatorTransportType(operator.getOperatorTransportType());
        Manager activeManager = identifyActiveManager(operator);
        if (activeManager != null) {
            rspOperatorDTO.setManagerId(activeManager.getManagerId());
        }
        rspOperatorDTO.setCreatedAt(operator.getCreatedAt());
        rspOperatorDTO.setUpdatedAt(operator.getUpdatedAt());
        rspOperatorDTO.setUpdatedBy(operator.getUpdatedBy());
        return rspOperatorDTO;
    }

    public Operator dtoToVOConverter(String action, RQTOperatorDTO rqtOperatorDTO, CustomizedUserDetails userDetails) {
        if (action == null || rqtOperatorDTO == null || userDetails == null) {
            return null;
        }
        Operator operator = new Operator();
        if (action.equalsIgnoreCase("Update")) {
            operator.setOperatorId(rqtOperatorDTO.getOperatorId());
        }
        operator.setOperatorName(rqtOperatorDTO.getOperatorName());
        operator.setOperatorTransportType(rqtOperatorDTO.getOperatorTransportType());
        if (action.equalsIgnoreCase("Save")) {
            operator.setCreatedAt(LocalDateTime.now());
        } else {
            operator.setCreatedAt(rqtOperatorDTO.getCreatedAt());
        }
        operator.setUpdatedAt(LocalDateTime.now());
        operator.setUpdatedBy(userDetails.getEmployeeId());

        return operator;
    }

    @Transactional (propagation = Propagation.REQUIRED)
    public RSPOperatorDTO saveOperator(RQTOperatorDTO rqtOperatorDTO, CustomizedUserDetails userDetails) {
        if (rqtOperatorDTO == null || userDetails == null) {
            throw new IllegalArgumentException("Payload request body and user context cannot be null");
        }
        Operator savingOperator = dtoToVOConverter("Save", rqtOperatorDTO, userDetails);
        Operator savedOperator = operatorRepo.save(savingOperator);
        return voToDTOConverter(savedOperator);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Operator internalFetchService(Integer operatorId) {
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
        Operator mutatedOperator = dtoToVOConverter("Update", rqtOperatorDTO, userDetails);
        Operator updatedOperator = operatorRepo.save(mutatedOperator);
        return voToDTOConverter(updatedOperator);
    }

    @Transactional (propagation = Propagation.REQUIRED)
    public ResponseEntity<String> deleteOperator(Integer operatorId) {
        if (operatorId == null) {
            return ResponseEntity.status(400).body("Operator ID parameter cannot be null");
        }
        operatorRepo.deleteById(operatorId);
        return ResponseEntity.status(200).body("Operator deleted successfully.");
    }

    public OperatorRequest fetchCarrierOption() {

        OperatorRequest operatorRequest = new OperatorRequest();

        for (CarrierOptionEnum carrierOptionEnum : CarrierOptionEnum.values()) {
            operatorRequest.setCarrierOptionEnumList(carrierOptionEnum);
        }

        return operatorRequest;
    }

    @Transactional(readOnly = true)
    public RSPOperatorDTO fetchByOperatorName(String operatorName) {
        if (operatorName == null || operatorName.trim().isEmpty()) {
            return null;
        }

        Operator fetchOperator = operatorRepo.findByOperatorName(operatorName);
        if(fetchOperator != null){
            return voToDTOConverter(fetchOperator);
        }
        return null;
    }
}
