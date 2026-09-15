package com.app.logistics.employee.controller;

import com.app.logistics.auth.authUtils.AuthDetails;
import com.app.logistics.common.dto.ApiResponse;
import com.app.logistics.common.validations.OnCreate;
import com.app.logistics.common.validations.OnUpdate;
import com.app.logistics.driver.dto.DriverProfileResponse;
import com.app.logistics.driver.dto.DriverRequest;
import com.app.logistics.driver.dto.DriverResponse;
import com.app.logistics.employee.dto.EmployeeProfileResponse;
import com.app.logistics.employee.dto.EmployeeRequest;
import com.app.logistics.employee.dto.EmployeeResponse;
import com.app.logistics.employee.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<EmployeeProfileResponse>> fetchDriver(@AuthenticationPrincipal AuthDetails authDetails) {
        EmployeeProfileResponse result = employeeService.fetchEmployeeProfile(authDetails);

        ApiResponse<EmployeeProfileResponse> apiResponse = new ApiResponse
                .Builder<EmployeeProfileResponse>(true, result)
                .message("Driver profile fetched successfully.")
                .timeStamp()
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/fetchall")
    public ResponseEntity<ApiResponse<Map<String, Object>>> fetchAllEmployee(
            @RequestParam(defaultValue = "1") int pageNo) {

        Map<String, Object> result = employeeService.fetchAllEmployee(pageNo);

        ApiResponse<Map<String, Object>> apiResponse = new ApiResponse
                .Builder<Map<String, Object>>(true, result)
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
                .message("New employee recorded and account for employee has been created successfully.")
                .timeStamp()
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<EmployeeResponse>> updateEmployee(
            @Validated(OnUpdate.class) @RequestBody EmployeeRequest employeeRequest,
            @AuthenticationPrincipal AuthDetails authDetails) {

        EmployeeResponse result = employeeService.updateEmployee(employeeRequest, authDetails);

        ApiResponse<EmployeeResponse> apiResponse = new ApiResponse
                .Builder<EmployeeResponse>(true, result)
                .message("Employee updated successfully.")
                .timeStamp()
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<Void>> deleteDriver(@RequestParam Integer employeeId) {
        employeeService.deleteEmployee(employeeId);

        ApiResponse<Void> apiResponse = new ApiResponse
                .Builder<Void>(true, null)
                .message("Employee deleted successfully.")
                .timeStamp()
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}