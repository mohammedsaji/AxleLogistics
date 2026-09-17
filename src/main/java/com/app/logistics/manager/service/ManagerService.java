package com.app.logistics.manager.service;

import com.app.logistics.account.dto.AccountResponse;
import com.app.logistics.account.entity.Account;
import com.app.logistics.account.service.AccountService;
import com.app.logistics.auth.authUtils.AuthDetails;
import com.app.logistics.common.exception.APIException;
import com.app.logistics.driver.dto.DriverResponse;
import com.app.logistics.driver.entity.Driver;
import com.app.logistics.manager.dto.ManagerProfileResponse;
import com.app.logistics.manager.entity.Manager;
import com.app.logistics.manager.dto.ManagerRequest;
import com.app.logistics.manager.dto.ManagerResponse;
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

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ManagerService {

    private final ManagerRepo managerRepo;
    private final ManagerMapper managerMapper;
    private final OperatorService operatorService;
    private final AccountService accountService;

    public ManagerService(ManagerRepo managerRepo,
                          ManagerMapper managerMapper,
                          @Lazy OperatorService operatorService,
                          @Lazy AccountService accountService) {
        this.managerRepo = managerRepo;
        this.managerMapper = managerMapper;
        this.operatorService = operatorService;
        this.accountService = accountService;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ManagerResponse saveManager(ManagerRequest managerRequest, String accountUsername, AuthDetails authDetails) {
        if (managerRequest == null || accountUsername == null || authDetails == null) {
            throw new APIException("Required metadata or payload context is missing", HttpStatus.BAD_REQUEST);
        }

        Account linkedAccount = accountService.saveUser(accountUsername);

        Operator operator = operatorService.internalFetchService(managerRequest.getOperatorId());
        if (operator == null) {
            throw new APIException("Operator ID " + managerRequest.getOperatorId() + " does not exist in the system.", HttpStatus.BAD_REQUEST);
        }

        Manager savingManager = managerMapper.toVO(managerRequest);
        savingManager.setAccount(linkedAccount);
        savingManager.setOperator(operator);
        savingManager.setUpdatedBy(authDetails.getEmployeeId());
        boolean doesOperatorHaveActiveManager = findAllActiveManager(managerRequest.getOperatorId()).stream().anyMatch(manager -> "ACTIVE".equals(manager.getManagerStatus()));
        if(!doesOperatorHaveActiveManager) {
            savingManager.setManagerStatus("ACTIVE");
        }else{
            savingManager.setManagerStatus("IN_ACTIVE");
        }
        Manager savedManager = managerRepo.save(savingManager);
        return managerMapper.toDTO(savedManager);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> fetchAllManager(Integer operatorId, int pageNo) {
        if (pageNo < 1) {
            pageNo = 1;
        }
        int elementCount = 10;
        Pageable pageable = PageRequest.of(pageNo - 1, elementCount, Sort.by("managerId"));
        Page<Manager> page = managerRepo.findByOperator_OperatorId(operatorId, pageable);

        Map<String, Object> valueMap = new HashMap<>();
        valueMap.put("managerList", page.getContent().stream().map(managerMapper::toDTO).collect(Collectors.toList()));
        valueMap.put("totalPages", page.getTotalPages());
        return valueMap;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Manager internalFetchService(Integer managerId) {
        if (managerId == null) {
            throw new APIException("Manager ID cannot be null", HttpStatus.BAD_REQUEST);
        }
        return managerRepo.findById(managerId)
                .orElseThrow(() -> new APIException("Manager not found for ID: " + managerId, HttpStatus.NOT_FOUND));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteManager(Integer managerId) {
        if (managerId == null) {
            throw new APIException("Manager ID parameter cannot be null", HttpStatus.BAD_REQUEST);
        }
        if (!managerRepo.existsById(managerId)) {
            throw new APIException("Manager not found for ID: " + managerId, HttpStatus.NOT_FOUND);
        }
        Manager manager = internalFetchService(managerId);
        if(manager!= null && manager.getManagerStatus().equals("ACTIVE")){
            alternateManagerToActivate(manager.getOperator().getOperatorId(),manager.getManagerId());
        }
        Account account = manager.getAccount();
        if(account != null){
            account.setEmployee(null);
            manager.setAccount(null);
            accountService.disableAccount(account.getAccountId());
        }
        managerRepo.deleteById(managerId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public ManagerResponse updateManager(ManagerRequest managerRequest, AuthDetails authDetails) {
        if (managerRequest == null) {
            throw new APIException("Manager request data payload cannot be null", HttpStatus.BAD_REQUEST);
        }

        Manager existingManager = managerRepo.findById(managerRequest.getManagerId())
                .orElseThrow(() -> new APIException("Manager ID " + managerRequest.getManagerId() + " not found.", HttpStatus.NOT_FOUND));


        Operator operator = operatorService.internalFetchService(managerRequest.getOperatorId());
        if (operator == null) {
            throw new APIException("Operator ID " + managerRequest.getOperatorId() + " does not exist in the system.", HttpStatus.BAD_REQUEST);
        }

        existingManager.setManagerName(managerRequest.getManagerName());
        existingManager.setManagerContactNo(managerRequest.getManagerContactNo());
        existingManager.setManagerStatus(managerRequest.getManagerStatus());
        existingManager.setOperator(operator);
        existingManager.setUpdatedAt(LocalDateTime.now());

        if (authDetails != null) {
            existingManager.setUpdatedBy(authDetails.getEmployeeId());
        }

        activeManagerOneness(existingManager);
        return managerMapper.toDTO(existingManager);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void activeManagerOneness(Manager mutatedManager) {
        if (mutatedManager == null || mutatedManager.getOperator() == null || mutatedManager.getOperator().getOperatorId() == null) {
            return;
        }
        List<Manager> managerList = managerRepo.findByOperator_OperatorId(mutatedManager.getOperator().getOperatorId());

        for (Manager manager : managerList) {
            if (manager.getManagerId() != null && manager.getManagerId().equals(mutatedManager.getManagerId())) {
                manager.setManagerStatus("ACTIVE");
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

    @Transactional(readOnly = true)
    public ManagerProfileResponse fetchManagerProfile(AuthDetails authDetails){
        AccountResponse accountResponse =  accountService.internalFetchService(authDetails.getAccount().getAccountId());
        Manager manager = findManagerByAccountID(authDetails.getAccount().getAccountId());

        ManagerProfileResponse managerProfileResponse =  new ManagerProfileResponse();
        managerProfileResponse.setAccountResponse(accountResponse);
        managerProfileResponse.setManagerResponse(managerMapper.toDTO(manager));
        managerProfileResponse.setOperatorResponse(operatorService.fetchOperator(manager.getOperator().getOperatorId()));
        return managerProfileResponse;
    }

    @Transactional(readOnly = true)
    public List<Manager> findAllActiveManager(Integer operatorId) {
        return managerRepo.findByOperator_OperatorId(operatorId);
    }

    @Transactional(readOnly = true)
    public Manager findActiveManager(Integer operatorId) {
        return managerRepo.findByActiveManager(operatorId).orElse(null);
    }

    @Transactional(readOnly = true)
    public ManagerResponse findActiveManagerInternalFetchService(Integer operatorId) {
        return managerMapper.toDTO(findActiveManager(operatorId));
    }

    @Transactional(readOnly = true)
    public Manager findManagerByAccountID(Integer accountId) {
        if(accountId == null){
            throw new APIException("Account Id provided should be null, required for manager fetching.",HttpStatus.BAD_REQUEST);
        }
        return managerRepo.findByAccount_AccountId(accountId).orElse(null);
    }

    public ManagerResponse findManagerByAccountID(AuthDetails authDetails){
        return managerMapper.toDTO(findManagerByAccountID(authDetails.getAccount().getAccountId()));
    }

    @Transactional( propagation = Propagation.REQUIRED)
    public void alternateManagerToActivate(Integer operatorId, Integer managerId){
        Manager managerToSetAsActive = managerRepo.findAlternateManagerToActivate(operatorId,managerId).orElseThrow(
                ()-> new APIException("Could not be deleted, because no Manager is available for this operator at this moment to set as activate on deleting current manager who is under active state.",HttpStatus.OK));
        managerToSetAsActive.setManagerStatus("ACTIVE");
    }
}