package com.app.logistics.employee.controller;

import com.app.logistics.auth.authUtils.AuthDetails;
import com.app.logistics.employee.dto.EmployeeRequest;
import com.app.logistics.employee.dto.EmployeeResponse;
import com.app.logistics.dto.MessageDTO.ResponseMessageDTO;
import com.app.logistics.employee.service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@Controller
@RequestMapping("logistic/employee")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService){
        this.employeeService = employeeService;
    }

    @GetMapping("/fetch")
    @ResponseBody
    public ResponseEntity<EmployeeResponse> fetchEmployee(@RequestParam Integer employeeId){
        EmployeeResponse result = employeeService.fetchEmployee(employeeId);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.noContent().build();
    }

    @GetMapping("/fetchall")
    @ResponseBody
    public ResponseMessageDTO fetchAllEmployee(@RequestParam int pageNo){
        return employeeService.fetchAllEmployee(pageNo);
    }

    @GetMapping("/fetchByName")
    @ResponseBody
    public ResponseEntity<EmployeeResponse> fetchByEmployeeName(@RequestParam String employeeName) {
        EmployeeResponse result = employeeService.fetchByEmployeeName(employeeName);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.noContent().build();
    }


    @PostMapping("/save")
    @ResponseBody
    public ResponseEntity<String> saveEmployee(@RequestBody Map<String,Object> employeeDetails, @AuthenticationPrincipal AuthDetails authDetails) throws IOException {

        EmployeeRequest employeeRequest = new EmployeeRequest();
        employeeRequest.setEmployeeName(employeeDetails.get("employeeName").toString());
        employeeRequest.setEmployeePhoneNo(employeeDetails.get("employeePhoneNo").toString());
        employeeRequest.setEmployeeDepartment(employeeDetails.get("employeeDepartment").toString());
        employeeRequest.setReportingManagerId(Integer.valueOf(employeeDetails.get("reportingManagerId").toString()));
        employeeRequest.setEmployeeStatus(employeeDetails.get("employeeStatus").toString());

        String username = employeeDetails.get("accountUserName").toString();

        return employeeService.saveEmployee(employeeRequest,username,authDetails);
    }
}