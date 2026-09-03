package com.app.logistics.employee.service;

import com.app.logistics.auth.authUtils.AuthDetails;
import com.app.logistics.auth.entity.Auth;
import com.app.logistics.auth.service.AuthService;
import com.app.logistics.common.exception.APIException;
import com.app.logistics.employee.dto.EmployeeRequest;
import com.app.logistics.employee.dto.EmployeeResponse;
import com.app.logistics.employee.entity.Employee;
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
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private final EmployeeRepo employeeRepo;
    private final EmployeeMapper employeeMapper;
    private final AuthService authService;

    public EmployeeService(EmployeeRepo employeeRepo,
                           EmployeeMapper employeeMapper,
                           AuthService authService) {
        this.employeeRepo = employeeRepo;
        this.employeeMapper = employeeMapper;
        this.authService = authService;
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
    public List<EmployeeResponse> fetchAllEmployee(int pageNo) {
        if (pageNo < 1) {
            pageNo = 1;
        }
        int elementCount = 25;
        Pageable pageable = PageRequest.of(pageNo - 1, elementCount, Sort.by("employeeId"));
        Page<Employee> page = employeeRepo.findAll(pageable);

        return page.getContent().stream()
                .map(employeeMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public EmployeeResponse saveEmployee(EmployeeRequest employeeRequest, String username, AuthDetails authDetails) {
        if (employeeRequest == null || username == null || authDetails == null) {
            throw new APIException("Required metadata or payload context is missing", HttpStatus.BAD_REQUEST);
        }

        Auth linkedAuth = authService.saveUser(username);

        Employee savingEmployee = employeeMapper.toVO(employeeRequest);
        savingEmployee.setAccountVO(linkedAuth);
        savingEmployee.setEmployeeJoiningDate(LocalDateTime.now());
        savingEmployee.setUpdatedBy(authDetails.getEmployeeId());

        Employee savedEmployee = employeeRepo.save(savingEmployee);
        return employeeMapper.toDTO(savedEmployee);
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
}