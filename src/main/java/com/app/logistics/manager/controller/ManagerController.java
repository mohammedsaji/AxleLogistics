package com.app.logistics.manager.controller;

import com.app.logistics.auth.authUtils.AuthDetails;
import com.app.logistics.common.dto.ApiResponse;
import com.app.logistics.common.validations.OnCreate;
import com.app.logistics.common.validations.OnUpdate;
import com.app.logistics.manager.dto.ManagerProfileResponse;
import com.app.logistics.manager.dto.ManagerRequest;
import com.app.logistics.manager.dto.ManagerResponse;
import com.app.logistics.manager.service.ManagerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("logistic/manager")
public class ManagerController {

    private final ManagerService managerService;

    public ManagerController(ManagerService managerService) {
        this.managerService = managerService;
    }

    @GetMapping("/fetch")
    public ResponseEntity<ApiResponse<ManagerResponse>> fetchManager(@RequestParam Integer managerId) {
        ManagerResponse result = managerService.fetchManager(managerId);

        ApiResponse<ManagerResponse> apiResponse = new ApiResponse
                .Builder<ManagerResponse>(true, result)
                .message("Manager fetched successfully.")
                .timeStamp()
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<ManagerProfileResponse>> fetchManager(@AuthenticationPrincipal AuthDetails authDetails) {
        ManagerProfileResponse result = managerService.fetchManagerProfile(authDetails);

        ApiResponse<ManagerProfileResponse> apiResponse = new ApiResponse
                .Builder<ManagerProfileResponse>(true, result)
                .message("Manager profile fetched successfully.")
                .timeStamp()
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/fetchall")
    public ResponseEntity<ApiResponse<Map<String, Object>>> fetchAllManager(
            @RequestParam Integer operatorId,
            @RequestParam(defaultValue = "1") int pageNo) {

        Map<String, Object> result = managerService.fetchAllManager(operatorId, pageNo);

        ApiResponse<Map<String, Object>> apiResponse = new ApiResponse
                .Builder<Map<String, Object>>(true, result)
                .message("Manager list fetched successfully.")
                .timeStamp()
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/fetchByName")
    public ResponseEntity<ApiResponse<ManagerResponse>> getManagerByName(@RequestParam String managerName) {
        ManagerResponse result = managerService.fetchByManagerName(managerName);

        ApiResponse<ManagerResponse> apiResponse = new ApiResponse
                .Builder<ManagerResponse>(true, result)
                .message("Manager fetched successfully.")
                .timeStamp()
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<ApiResponse<ManagerResponse>> saveManager(
            @Validated(OnCreate.class) @RequestBody ManagerRequest managerRequest,
            @RequestParam String accountUserName,
            @AuthenticationPrincipal AuthDetails authDetails) {

        ManagerResponse result = managerService.saveManager(managerRequest, accountUserName, authDetails);

        ApiResponse<ManagerResponse> apiResponse = new ApiResponse
                .Builder<ManagerResponse>(true, result)
                .message("New manager recorded and account for manager has been created successfully.")
                .timeStamp()
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<Void>> deleteManager(@RequestParam Integer managerId) {
        managerService.deleteManager(managerId);

        ApiResponse<Void> apiResponse = new ApiResponse
                .Builder<Void>(true, null)
                .message("Manager deleted successfully.")
                .timeStamp()
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<ManagerResponse>> updateManager(
            @Validated(OnUpdate.class) @RequestBody ManagerRequest managerRequest,
            @AuthenticationPrincipal AuthDetails authDetails) {

        ManagerResponse result = managerService.updateManager(managerRequest, authDetails);

        ApiResponse<ManagerResponse> apiResponse = new ApiResponse
                .Builder<ManagerResponse>(true, result)
                .message("Manager updated successfully.")
                .timeStamp()
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}