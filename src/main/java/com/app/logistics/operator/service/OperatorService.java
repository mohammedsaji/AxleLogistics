package com.app.logistics.operator.service;

import com.app.logistics.Enum.CarrierOptionEnum;
import com.app.logistics.common.dto.OperatorRequest;
import com.app.logistics.common.exception.APIException;
import com.app.logistics.manager.entity.Manager;
import com.app.logistics.manager.service.ManagerService;
import com.app.logistics.operator.entity.Operator;
import com.app.logistics.operator.repo.OperatorRepo;
import com.app.logistics.operator.utils.OperatorMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OperatorService {

    private final OperatorRepo operatorRepo;
    private final OperatorMapper operatorMapper;
    private final ManagerService managerService;

    public OperatorService(OperatorRepo operatorRepo,
                           OperatorMapper operatorMapper,
                           @Lazy ManagerService managerService) {
        this.operatorRepo = operatorRepo;
        this.operatorMapper = operatorMapper;
        this.managerService = managerService;
    }

    @Transactional(readOnly = true)
    public List<com.app.logistics.operator.dto.OperatorResponse> fetchAllOperator(String operatorTransportType, int pageNo) {
        if (pageNo < 1) {
            pageNo = 1;
        }
        int elementCount = 25;
        Pageable pageable = PageRequest.of(pageNo - 1, elementCount, Sort.by("operatorId").descending());
        Page<Operator> page = operatorRepo.findByOperatorTransportType(operatorTransportType, pageable);

        return page.getContent().stream()
                .map(this::toResponseWithActiveManager)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public com.app.logistics.operator.dto.OperatorResponse fetchOperator(Integer operatorId) {
        if (operatorId == null) {
            throw new APIException("Operator ID cannot be null", HttpStatus.BAD_REQUEST);
        }
        Operator operator = operatorRepo.findById(operatorId)
                .orElseThrow(() -> new APIException("Operator not found for ID: " + operatorId, HttpStatus.NOT_FOUND));
        return toResponseWithActiveManager(operator);
    }

    public Manager identifyActiveManager(Operator operator) {
        if (operator == null || operator.getManagerVOList() == null) {
            return new Manager();
        }
        Manager activeManager = new Manager();
        for (Manager manager : operator.getManagerVOList()) {
            if (manager != null && manager.getManagerStatus() != null && manager.getManagerStatus().equalsIgnoreCase("ACTIVE")) {
                activeManager = manager;
            }
        }
        return activeManager;
    }

    private com.app.logistics.operator.dto.OperatorResponse toResponseWithActiveManager(Operator operator) {
        com.app.logistics.operator.dto.OperatorResponse response = operatorMapper.toDTO(operator);
        response.setManagerId(identifyActiveManager(operator).getManagerId());
        return response;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public com.app.logistics.operator.dto.OperatorResponse saveOperator(com.app.logistics.operator.dto.OperatorRequest operatorRequest, com.app.logistics.auth.authUtils.AuthDetails authDetails) {
        if (operatorRequest == null || authDetails == null) {
            throw new APIException("Payload request body and user context cannot be null", HttpStatus.BAD_REQUEST);
        }

        managerService.fetchManager(operatorRequest.getManagerId());

        Operator savingOperator = operatorMapper.toVO(operatorRequest);
        savingOperator.setUpdatedBy(authDetails.getEmployeeId());

        Operator savedOperator = operatorRepo.save(savingOperator);
        return toResponseWithActiveManager(savedOperator);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Operator internalFetchService(Integer operatorId) {
        if (operatorId == null) {
            throw new APIException("Operator ID cannot be null", HttpStatus.BAD_REQUEST);
        }
        return operatorRepo.findById(operatorId)
                .orElseThrow(() -> new APIException("Operator not found for ID: " + operatorId, HttpStatus.NOT_FOUND));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public com.app.logistics.operator.dto.OperatorResponse updateOperator(com.app.logistics.operator.dto.OperatorRequest operatorRequest, com.app.logistics.auth.authUtils.AuthDetails authDetails) {
        if (operatorRequest == null) {
            throw new APIException("Operator request data payload cannot be null", HttpStatus.BAD_REQUEST);
        }

        managerService.fetchManager(operatorRequest.getManagerId());

        Operator mutatedOperator = operatorMapper.toVO(operatorRequest);
        if (authDetails != null) {
            mutatedOperator.setUpdatedBy(authDetails.getEmployeeId());
        }

        Operator updatedOperator = operatorRepo.save(mutatedOperator);
        return toResponseWithActiveManager(updatedOperator);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteOperator(Integer operatorId) {
        if (operatorId == null) {
            throw new APIException("Operator ID parameter cannot be null", HttpStatus.BAD_REQUEST);
        }
        if (!operatorRepo.existsById(operatorId)) {
            throw new APIException("Operator not found for ID: " + operatorId, HttpStatus.NOT_FOUND);
        }
        operatorRepo.deleteById(operatorId);
    }

    public OperatorRequest fetchCarrierOption() {
        OperatorRequest operatorRequest = new OperatorRequest();
        for (CarrierOptionEnum carrierOptionEnum : CarrierOptionEnum.values()) {
            operatorRequest.setCarrierOptionEnumList(carrierOptionEnum);
        }
        return operatorRequest;
    }

    @Transactional(readOnly = true)
    public com.app.logistics.operator.dto.OperatorResponse fetchByOperatorName(String operatorName) {
        if (operatorName == null || operatorName.trim().isEmpty()) {
            throw new APIException("Operator name cannot be empty", HttpStatus.BAD_REQUEST);
        }
        Operator operator = operatorRepo.findByOperatorName(operatorName);
        if (operator == null) {
            throw new APIException("Operator not found for name: " + operatorName, HttpStatus.NOT_FOUND);
        }
        return toResponseWithActiveManager(operator);
    }
}