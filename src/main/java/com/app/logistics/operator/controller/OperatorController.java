package com.app.logistics.operator.controller;

import com.app.logistics.auth.authUtils.AuthDetails;
import com.app.logistics.common.dto.ApiResponse;
import com.app.logistics.common.validations.OnCreate;
import com.app.logistics.common.validations.OnUpdate;
import com.app.logistics.operator.dto.OperatorRequest;
import com.app.logistics.operator.dto.OperatorResponse;
import com.app.logistics.operator.service.OperatorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("logistic/operator")
public class OperatorController {

    private final OperatorService operatorService;

    public OperatorController(OperatorService operatorService) {
        this.operatorService = operatorService;
    }

    @GetMapping("/fetch")
    public ResponseEntity<ApiResponse<OperatorResponse>> fetchOperator(@RequestParam Integer operatorId) {
        OperatorResponse result = operatorService.fetchOperator(operatorId);

        ApiResponse<OperatorResponse> apiResponse = new ApiResponse
                .Builder<OperatorResponse>(true, result)
                .message("Operator fetched successfully.")
                .timeStamp()
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/fetchall")
    public ResponseEntity<ApiResponse<List<OperatorResponse>>> fetchAllOperator(
            @RequestParam String operatorTransportType,
            @RequestParam(defaultValue = "1") int pageNo) {

        List<OperatorResponse> result = operatorService.fetchAllOperator(operatorTransportType, pageNo);

        ApiResponse<List<OperatorResponse>> apiResponse = new ApiResponse
                .Builder<List<OperatorResponse>>(true, result)
                .message("Operator list fetched successfully.")
                .timeStamp()
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/fetchByName")
    public ResponseEntity<ApiResponse<OperatorResponse>> getOperatorByName(@RequestParam String operatorName) {
        OperatorResponse result = operatorService.fetchByOperatorName(operatorName);

        ApiResponse<OperatorResponse> apiResponse = new ApiResponse
                .Builder<OperatorResponse>(true, result)
                .message("Operator fetched successfully.")
                .timeStamp()
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<ApiResponse<OperatorResponse>> saveOperator(
            @Validated(OnCreate.class) @RequestBody OperatorRequest operatorRequest,
            @AuthenticationPrincipal AuthDetails authDetails) {

        OperatorResponse result = operatorService.saveOperator(operatorRequest, authDetails);

        ApiResponse<OperatorResponse> apiResponse = new ApiResponse
                .Builder<OperatorResponse>(true, result)
                .message("Operator created successfully.")
                .timeStamp()
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<OperatorResponse>> updateOperator(
            @Validated(OnUpdate.class) @RequestBody OperatorRequest operatorRequest,
            @AuthenticationPrincipal AuthDetails authDetails) {

        OperatorResponse result = operatorService.updateOperator(operatorRequest, authDetails);

        ApiResponse<OperatorResponse> apiResponse = new ApiResponse
                .Builder<OperatorResponse>(true, result)
                .message("Operator updated successfully.")
                .timeStamp()
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{operatorId}")
    public ResponseEntity<ApiResponse<Void>> deleteOperator(@PathVariable Integer operatorId) {
        operatorService.deleteOperator(operatorId);

        ApiResponse<Void> apiResponse = new ApiResponse
                .Builder<Void>(true, null)
                .message("Operator deleted successfully.")
                .timeStamp()
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/plans")
    public ResponseEntity<ApiResponse<com.app.logistics.common.dto.OperatorRequest>> fetchCarrierOptions() {
        com.app.logistics.common.dto.OperatorRequest result = operatorService.fetchCarrierOption();

        ApiResponse<com.app.logistics.common.dto.OperatorRequest> apiResponse = new ApiResponse
                .Builder<com.app.logistics.common.dto.OperatorRequest>(true, result)
                .message("Carrier options fetched successfully.")
                .timeStamp()
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}