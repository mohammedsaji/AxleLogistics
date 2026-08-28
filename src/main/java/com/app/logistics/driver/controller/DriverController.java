package com.app.logistics.driver.controller;

import com.app.logistics.auth.authUtils.AuthDetails;
import com.app.logistics.common.dto.ApiResponse;
import com.app.logistics.common.validations.OnCreate;
import com.app.logistics.common.validations.OnUpdate;
import com.app.logistics.driver.dto.DriverRequest;
import com.app.logistics.driver.dto.DriverResponse;
import com.app.logistics.driver.service.DriverService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("logistic/driver")
public class DriverController {

    private final DriverService driverService;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @GetMapping("/fetch")
    public ResponseEntity<ApiResponse<DriverResponse>> fetchDriver(@RequestParam Integer driverId) {
        DriverResponse result = driverService.fetchDriver(driverId);

        ApiResponse<DriverResponse> apiResponse = new ApiResponse
                .Builder<DriverResponse>(true, result)
                .message("Driver fetched successfully.")
                .timeStamp()
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/fetchall")
    public ResponseEntity<ApiResponse<List<DriverResponse>>> fetchAllDriver(
            @RequestParam Integer operatorId,
            @RequestParam(defaultValue = "1") int pageNo) {

        List<DriverResponse> result = driverService.fetchAllDriver(operatorId, pageNo);

        ApiResponse<List<DriverResponse>> apiResponse = new ApiResponse
                .Builder<List<DriverResponse>>(true, result)
                .message("Driver list fetched successfully.")
                .timeStamp()
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<ApiResponse<DriverResponse>> saveDriver(
            @Validated(OnCreate.class) @RequestBody DriverRequest driverRequest,
            @RequestParam String accountUserName,
            @AuthenticationPrincipal AuthDetails authDetails) {

        DriverResponse result = driverService.saveDriver(driverRequest, accountUserName, authDetails);

        ApiResponse<DriverResponse> apiResponse = new ApiResponse
                .Builder<DriverResponse>(true, result)
                .message("Driver created successfully.")
                .timeStamp()
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<DriverResponse>> updateDriver(
            @Validated(OnUpdate.class) @RequestBody DriverRequest driverRequest,
            @AuthenticationPrincipal AuthDetails authDetails) {

        DriverResponse result = driverService.updateDriver(driverRequest, authDetails);

        ApiResponse<DriverResponse> apiResponse = new ApiResponse
                .Builder<DriverResponse>(true, result)
                .message("Driver updated successfully.")
                .timeStamp()
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{driverId}")
    public ResponseEntity<ApiResponse<Void>> deleteDriver(@PathVariable Integer driverId) {
        driverService.deleteDriver(driverId);

        ApiResponse<Void> apiResponse = new ApiResponse
                .Builder<Void>(true, null)
                .message("Driver deleted successfully.")
                .timeStamp()
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/fetchByName")
    public ResponseEntity<ApiResponse<DriverResponse>> fetchByDriverName(@RequestParam String driverName) {
        DriverResponse result = driverService.findByDriverName(driverName);

        ApiResponse<DriverResponse> apiResponse = new ApiResponse
                .Builder<DriverResponse>(true, result)
                .message("Driver fetched successfully.")
                .timeStamp()
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}