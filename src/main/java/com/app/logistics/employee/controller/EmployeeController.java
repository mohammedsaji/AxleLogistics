package com.app.logistics.employee.controller;

import com.app.logistics.auth.authUtils.AuthDetails;
import com.app.logistics.common.dto.ApiResponse;
import com.app.logistics.common.validations.OnCreate;
import com.app.logistics.employee.dto.EmployeeRequest;
import com.app.logistics.employee.dto.EmployeeResponse;
import com.app.logistics.employee.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("logistic/employee")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/fetch")
    public ResponseEntity<ApiResponse<EmployeeResponse>> fetchEmployee(@RequestParam Integer employeeId) {
        EmployeeResponse result = employeeService.fetchEmployee(employeeId);

        ApiResponse<EmployeeResponse> apiResponse = new ApiResponse
                .Builder<EmployeeResponse>(true, result)
                .message("Employee fetched successfully.")
                .timeStamp()
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/fetchall")
    public ResponseEntity<ApiResponse<List<EmployeeResponse>>> fetchAllEmployee(
            @RequestParam(defaultValue = "1") int pageNo) {

        List<EmployeeResponse> result = employeeService.fetchAllEmployee(pageNo);

        ApiResponse<List<EmployeeResponse>> apiResponse = new ApiResponse
                .Builder<List<EmployeeResponse>>(true, result)
                .message("Employee list fetched successfully.")
                .timeStamp()
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/fetchByName")
    public ResponseEntity<ApiResponse<EmployeeResponse>> fetchByEmployeeName(@RequestParam String employeeName) {
        EmployeeResponse result = employeeService.fetchByEmployeeName(employeeName);

        ApiResponse<EmployeeResponse> apiResponse = new ApiResponse
                .Builder<EmployeeResponse>(true, result)
                .message("Employee fetched successfully.")
                .timeStamp()
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<ApiResponse<EmployeeResponse>> saveEmployee(
            @Validated(OnCreate.class) @RequestBody EmployeeRequest employeeRequest,
            @RequestParam String accountUserName,
            @AuthenticationPrincipal AuthDetails authDetails) {

        EmployeeResponse result = employeeService.saveEmployee(employeeRequest, accountUserName, authDetails);

        ApiResponse<EmployeeResponse> apiResponse = new ApiResponse
                .Builder<EmployeeResponse>(true, result)
                .message("Employee created successfully.")
                .timeStamp()
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }
}