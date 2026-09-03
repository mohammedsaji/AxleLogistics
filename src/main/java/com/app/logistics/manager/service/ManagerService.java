package com.app.logistics.manager.service;

import com.app.logistics.auth.authUtils.AuthDetails;
import com.app.logistics.auth.entity.Auth;
import com.app.logistics.auth.service.AuthService;
import com.app.logistics.common.exception.APIException;
import com.app.logistics.manager.dto.ManagerRequest;
import com.app.logistics.manager.dto.ManagerResponse;
import com.app.logistics.manager.entity.Manager;
import com.app.logistics.manager.repo.ManagerRepo;
import com.app.logistics.manager.utils.ManagerMapper;
import com.app.logistics.operator.entity.Operator;
import com.app.logistics.operator.service.OperatorService;
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
public class ManagerService {

    private final ManagerRepo managerRepo;
    private final ManagerMapper managerMapper;
    private final OperatorService operatorService;
    private final AuthService authService;

    public ManagerService(ManagerRepo managerRepo,
                          ManagerMapper managerMapper,
                          @Lazy OperatorService operatorService,
                          @Lazy AuthService authService) {
        this.managerRepo = managerRepo;
        this.managerMapper = managerMapper;
        this.operatorService = operatorService;
        this.authService = authService;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ManagerResponse saveManager(ManagerRequest managerRequest, String username, AuthDetails authDetails) {
        if (managerRequest == null || username == null || authDetails == null) {
            throw new APIException("Required metadata or payload context is missing", HttpStatus.BAD_REQUEST);
        }

        Auth linkedAuth = authService.saveUser(username);

        Operator operator = operatorService.internalFetchService(managerRequest.getOperatorId());
        if (operator == null) {
            throw new APIException("Operator ID " + managerRequest.getOperatorId() + " does not exist in the system.", HttpStatus.BAD_REQUEST);
        }

        Manager savingManager = managerMapper.toVO(managerRequest);
        savingManager.setAccountVO(linkedAuth);
        savingManager.setOperatorVO(operator);
        savingManager.setUpdatedBy(authDetails.getEmployeeId());

        Manager savedManager = managerRepo.save(savingManager);
        return managerMapper.toDTO(savedManager);
    }

    @Transactional(readOnly = true)
    public List<ManagerResponse> fetchAllManager(Integer operatorId, int pageNo) {
        if (pageNo < 1) {
            pageNo = 1;
        }
        int elementCount = 10;
        Pageable pageable = PageRequest.of(pageNo - 1, elementCount, Sort.by("managerId"));
        Page<Manager> page = managerRepo.findByOperatorVO_OperatorId(operatorId, pageable);

        return page.getContent().stream()
                .map(managerMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteManager(Integer managerId) {
        if (managerId == null) {
            throw new APIException("Manager ID parameter cannot be null", HttpStatus.BAD_REQUEST);
        }
        if (!managerRepo.existsById(managerId)) {
            throw new APIException("Manager not found for ID: " + managerId, HttpStatus.NOT_FOUND);
        }
        managerRepo.deleteById(managerId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public ManagerResponse updateManager(ManagerRequest managerRequest, AuthDetails authDetails) {
        if (managerRequest == null) {
            throw new APIException("Manager request data payload cannot be null", HttpStatus.BAD_REQUEST);
        }

        Operator operator = operatorService.internalFetchService(managerRequest.getOperatorId());
        if (operator == null) {
            throw new APIException("Operator ID " + managerRequest.getOperatorId() + " does not exist in the system.", HttpStatus.BAD_REQUEST);
        }

        Manager mutatedManager = managerMapper.toVO(managerRequest);
        mutatedManager.setOperatorVO(operator);
        if (authDetails != null) {
            mutatedManager.setUpdatedBy(authDetails.getEmployeeId());
        }

        activeManagerOneness(mutatedManager);
        Manager updatedManager = managerRepo.save(mutatedManager);
        return managerMapper.toDTO(updatedManager);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void activeManagerOneness(Manager mutatedManager) {
        if (mutatedManager == null || mutatedManager.getOperatorVO() == null || mutatedManager.getOperatorVO().getOperatorId() == null) {
            return;
        }
        List<Manager> managerList = managerRepo.findByOperatorVO_OperatorId(mutatedManager.getOperatorVO().getOperatorId());

        for (Manager manager : managerList) {
            if (manager.getManagerId() != null && manager.getManagerId().equals(mutatedManager.getManagerId())) {
                manager.setManagerStatus("ACTIVE");
                mutatedManager.setManagerStatus("ACTIVE");
            } else {
                manager.setManagerStatus("IN_ACTIVE");
            }
        }
        managerRepo.saveAll(managerList);
    }

    @Transactional(readOnly = true)
    public ManagerResponse fetchManager(Integer managerId) {
        if (managerId == null) {
            throw new APIException("Manager ID cannot be null", HttpStatus.BAD_REQUEST);
        }
        Manager manager = managerRepo.findById(managerId)
                .orElseThrow(() -> new APIException("Manager not found for ID: " + managerId, HttpStatus.NOT_FOUND));
        return managerMapper.toDTO(manager);
    }

    @Transactional(readOnly = true)
    public ManagerResponse fetchByManagerName(String managerName) {
        if (managerName == null || managerName.trim().isEmpty()) {
            throw new APIException("Manager name cannot be empty", HttpStatus.BAD_REQUEST);
        }
        Manager manager = managerRepo.findByManagerName(managerName);
        if (manager == null) {
            throw new APIException("Manager not found for name: " + managerName, HttpStatus.NOT_FOUND);
        }
        return managerMapper.toDTO(manager);
    }
}