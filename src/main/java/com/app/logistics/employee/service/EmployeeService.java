package com.app.logistics.employee.service;

import com.app.logistics.auth.service.AuthService;
import com.app.logistics.employee.dto.EmployeeRequest;
import com.app.logistics.employee.dto.EmployeeResponse;
import com.app.logistics.dto.MessageDTO.ResponseMessageDTO;
import com.app.logistics.employee.repo.EmployeeRepo;
import com.app.logistics.authUtils.CustomizedUserDetails;
import com.app.logistics.auth.entity.Auth;
import com.app.logistics.employee.entity.Employee;
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
public class EmployeeService {

    private final EmployeeRepo employeeRepo;

    private final AuthService authService;

    public EmployeeService(EmployeeRepo employeeRepo,
                           AuthService authService){
        this.employeeRepo = employeeRepo;
        this.authService = authService;
    }

    @Transactional(readOnly = true)
    public EmployeeResponse fetchEmployee(Integer employeeId){
        Employee fetchEmployee = employeeRepo.findById(employeeId).orElse(null);
        return voToDTOConverter(fetchEmployee);
    }

    @Transactional(readOnly = true)
    public ResponseMessageDTO fetchAllEmployee(int pageNo) {
        if (pageNo < 1) {
            pageNo = 1;
        }
        int elementCount = 25;
        Pageable pageable = PageRequest.of(pageNo-1, elementCount, Sort.by("employeeId"));
        Page<Employee> page = employeeRepo.findAll(pageable);
        List<Employee> employeeList = page.getContent();
        List<EmployeeResponse> employeeResponseList = new ArrayList<>();

        for (Employee employee : employeeList) {
            employeeResponseList.add(voToDTOConverter(employee));
        }
        ResponseMessageDTO responseMessageDTO = new ResponseMessageDTO();
        responseMessageDTO.setStatusCode(200);
        responseMessageDTO.setValue("EmployeeList", employeeResponseList);
        responseMessageDTO.setValue("TotalPages",page.getTotalPages());
        responseMessageDTO.setValue("TotalElements",page.getTotalElements());
        return responseMessageDTO;
    }

    public EmployeeResponse voToDTOConverter(Employee employee) {
        if (employee == null) {
            return new EmployeeResponse();
        }
        EmployeeResponse employeeResponse = new EmployeeResponse();

        employeeResponse.setEmployeeId(employee.getEmployeeId());
        employeeResponse.setEmployeeName(employee.getEmployeeName());
        employeeResponse.setEmployeePhoneNo(employee.getEmployeePhoneNo());
        employeeResponse.setEmployeeDepartment(employee.getEmployeeDepartment());
        employeeResponse.setEmployeeJoiningDate(employee.getEmployeeJoiningDate());
        employeeResponse.setEmployeeStatus(employee.getEmployeeStatus());
        employeeResponse.setReportingManagerId(employee.getReportingManagerId());
        employeeResponse.setAccountId(employee.getAccountVO().getAccountId());
        employeeResponse.setAccountVO(employee.getAccountVO());
        employeeResponse.setCreatedAt(employee.getCreatedAt());
        employeeResponse.setUpdatedAt(employee.getUpdatedAt());

        return employeeResponse;
    }


    public Employee dtoToVOConverter(String action, EmployeeRequest employeeRequest, CustomizedUserDetails userDetails){
        if (action == null || employeeRequest == null || userDetails == null) {
            return new Employee();
        }
        Employee employee = new Employee();
        if(action.equalsIgnoreCase("SAVE")){
            employee.setEmployeeId(employeeRequest.getEmployeeId());
        }
        employee.setEmployeeName(employeeRequest.getEmployeeName());
        employee.setEmployeePhoneNo(employeeRequest.getEmployeePhoneNo());
        employee.setEmployeeDepartment(employeeRequest.getEmployeeDepartment());
        employee.setEmployeeStatus(employeeRequest.getEmployeeStatus());
        employee.setReportingManagerId(employeeRequest.getReportingManagerId());
        if(action.equalsIgnoreCase("SAVE")){
            employee.setEmployeeJoiningDate(LocalDateTime.now());
            employee.setCreatedAt(LocalDateTime.now());
        }else{
            employee.setCreatedAt(employeeRequest.getCreatedAt());
        }
        employee.setUpdatedAt(LocalDateTime.now());
        employee.setUpdatedBy(userDetails.getEmployeeId());

        return employee;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public ResponseEntity<String> saveEmployee(EmployeeRequest employeeRequest, String username, CustomizedUserDetails userDetails){
        if (employeeRequest == null || username == null || userDetails == null) {
            return ResponseEntity.status(400).body("Required metadata or payload context is missing");
        }
        Auth savedAuth = authService.saveUser(username);
        if(savedAuth != null){
            Employee savingEmployee = dtoToVOConverter("SAVE", employeeRequest,userDetails);
            savingEmployee.setAccountVO(savedAuth);
            Employee savedEmployee = employeeRepo.save(savingEmployee);
            if(savedEmployee.getEmployeeId() != null && savedEmployee.getEmployeeId() > 0){
                return ResponseEntity.status(200).body("Account created Successfully.");
            }else{
                return ResponseEntity.status(400).body("Invalid or Bad Request");
            }
        }
        return ResponseEntity.status(500).body("Internal server error");
    }

    @Transactional(readOnly = true)
    public EmployeeResponse fetchByEmployeeName(String employeeName) {
        if (employeeName == null || employeeName.trim().isEmpty()) {
            return null;
        }

        Employee fetchEmployee = employeeRepo.findByEmployeeName(employeeName);
        if (fetchEmployee != null) {
            return voToDTOConverter(fetchEmployee);
        }

        return null;
    }
}
