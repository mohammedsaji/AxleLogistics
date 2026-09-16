package com.app.logistics.employee.service;

import com.app.logistics.account.dto.AccountResponse;
import com.app.logistics.account.service.AccountService;
import com.app.logistics.auth.authUtils.AuthDetails;
import com.app.logistics.account.entity.Account;
import com.app.logistics.common.exception.APIException;
import com.app.logistics.driver.dto.DriverProfileResponse;
import com.app.logistics.driver.entity.Driver;
import com.app.logistics.employee.dto.EmployeeProfileResponse;
import com.app.logistics.employee.entity.Employee;
import com.app.logistics.employee.entity.Employee;
import com.app.logistics.employee.dto.EmployeeRequest;
import com.app.logistics.employee.dto.EmployeeResponse;
import com.app.logistics.employee.repo.EmployeeRepo;
import com.app.logistics.employee.utils.EmployeeMapper;
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

@Service
public class EmployeeService {

    private final EmployeeRepo employeeRepo;
    private final EmployeeMapper employeeMapper;
    private final AccountService accountService;

    public EmployeeService(EmployeeRepo employeeRepo,
                           EmployeeMapper employeeMapper,
                           AccountService accountService) {
        this.employeeRepo = employeeRepo;
        this.employeeMapper = employeeMapper;
        this.accountService = accountService;
    }

    @Transactional(readOnly = true)
    public EmployeeResponse fetchEmployee(Integer employeeId) {
        if (employeeId == null) {
            throw new APIException("Employee ID cannot be null", HttpStatus.BAD_REQUEST);
        }
        Employee employee = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new APIException("Employee not found for ID: " + employeeId, HttpStatus.NOT_FOUND));
        return employeeMapper.toDTO(employee);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> fetchAllEmployee(int pageNo) {
        if (pageNo < 1) {
            pageNo = 1;
        }
        int elementCount = 10;
        Pageable pageable = PageRequest.of(pageNo - 1, elementCount, Sort.by("employeeId"));
        Page<Employee> page = employeeRepo.findAll(pageable);

        Map<String, Object> valueMap = new HashMap<>();
        valueMap.put("employeeList", page.getContent().stream().map(employeeMapper::toDTO).collect(Collectors.toList()));
        valueMap.put("totalPages", page.getTotalPages());
        return valueMap;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public EmployeeResponse saveEmployee(EmployeeRequest employeeRequest, String username, AuthDetails authDetails) {
        if (employeeRequest == null || username == null || authDetails == null) {
            throw new APIException("Required metadata or payload context is missing", HttpStatus.BAD_REQUEST);
        }

        Account linkedAccount = accountService.saveUser(username);

        Employee savingEmployee = employeeMapper.toVO(employeeRequest);
        savingEmployee.setAccount(linkedAccount);
        savingEmployee.setEmployeeJoiningDate(LocalDateTime.now());
        if(employeeRequest.getReportingManagerId() != null){
            savingEmployee.setReportingManagerId(employeeRequest.getReportingManagerId());
        }
        savingEmployee.setUpdatedBy(authDetails.getEmployeeId());

        Employee savedEmployee = employeeRepo.save(savingEmployee);
        return employeeMapper.toDTO(savedEmployee);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public EmployeeResponse updateEmployee(EmployeeRequest employeeRequest, AuthDetails authDetails) {
        if (employeeRequest == null) {
            throw new APIException("Employee request payload cannot be null", HttpStatus.BAD_REQUEST);
        }

        if (employeeRequest.getEmployeeId() == null) {
            throw new APIException("Employee ID is required for update", HttpStatus.BAD_REQUEST);
        }

        Employee existingEmployee = employeeRepo.findById(employeeRequest.getEmployeeId())
                .orElseThrow(() -> new APIException("Employee not found for ID: " + employeeRequest.getEmployeeId(), HttpStatus.NOT_FOUND));

        if (employeeRequest.getReportingManagerId() != null) {
            boolean managerExists = employeeRepo.existsById(employeeRequest.getReportingManagerId());
            if (!managerExists) {
                throw new APIException("Reporting manager not found for ID: " + employeeRequest.getReportingManagerId(), HttpStatus.BAD_REQUEST);
            }
        }

        existingEmployee.setEmployeeName(employeeRequest.getEmployeeName());
        existingEmployee.setEmployeePhoneNo(employeeRequest.getEmployeePhoneNo());
        existingEmployee.setEmployeeDepartment(employeeRequest.getEmployeeDepartment());
        existingEmployee.setEmployeeStatus(employeeRequest.getEmployeeStatus());
        existingEmployee.setReportingManagerId(employeeRequest.getReportingManagerId());

        if (employeeRequest.getEmployeeJoiningDate() != null) {
            existingEmployee.setEmployeeJoiningDate(employeeRequest.getEmployeeJoiningDate());
        }

        existingEmployee.setUpdatedAt(LocalDateTime.now());
        if (authDetails != null) {
            existingEmployee.setUpdatedBy(authDetails.getEmployeeId());
        }

        Employee updatedEmployee = employeeRepo.save(existingEmployee);
        return employeeMapper.toDTO(updatedEmployee);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Employee internalFetchService(Integer employeeId) {
        if (employeeId == null) {
            throw new APIException("Employee ID cannot be null", HttpStatus.BAD_REQUEST);
        }
        return employeeRepo.findById(employeeId)
                .orElseThrow(() -> new APIException("Employee not found for ID: " + employeeId, HttpStatus.NOT_FOUND));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteEmployee(Integer employeeId) {
        if (employeeId == null) {
            throw new APIException("Employee identifier parameter cannot be null", HttpStatus.BAD_REQUEST);
        }
        if (!employeeRepo.existsById(employeeId)) {
            throw new APIException("Employee not found for ID: " + employeeId, HttpStatus.NOT_FOUND);
        }
        Employee employee = internalFetchService(employeeId);
        Account account = employee.getAccount();
        if(account != null){
            account.setEmployee(null);
            employee.setAccount(null);
            accountService.disableAccount(account.getAccountId());
        }
        employeeRepo.deleteById(employeeId);
    }

    @Transactional(readOnly = true)
    public EmployeeResponse fetchByEmployeeName(String employeeName) {
        if (employeeName == null || employeeName.trim().isEmpty()) {
            throw new APIException("Employee name cannot be empty", HttpStatus.BAD_REQUEST);
        }
        Employee employee = employeeRepo.findByEmployeeName(employeeName);
        if (employee == null) {
            throw new APIException("Employee not found for name: " + employeeName, HttpStatus.NOT_FOUND);
        }
        return employeeMapper.toDTO(employee);
    }

    @Transactional(readOnly = true)
    public EmployeeProfileResponse fetchEmployeeProfile(AuthDetails authDetails){
        AccountResponse accountResponse =  accountService.internalFetchService(authDetails.getAccount().getAccountId());
        Employee employee = findEmployeeByAccountID(authDetails.getAccount().getAccountId());
        EmployeeProfileResponse employeeProfileResponse =  new EmployeeProfileResponse();
        employeeProfileResponse.setAccountResponse(accountResponse);
        employeeProfileResponse.setEmployeeResponse(employeeMapper.toDTO(employee));

        return employeeProfileResponse;
    }

    @Transactional(readOnly = true)
    public Employee findEmployeeByAccountID(Integer accountId) {
        if(accountId == null){
            throw new APIException("Account Id provided should be null, required for employee fetching.",HttpStatus.BAD_REQUEST);
        }
        return employeeRepo.findByAccount_AccountId(accountId).orElse(null);
    }
}