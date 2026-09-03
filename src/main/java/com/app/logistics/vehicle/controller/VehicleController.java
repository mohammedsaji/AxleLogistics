package com.app.logistics.vehicle.controller;

import com.app.logistics.auth.authUtils.AuthDetails;
import com.app.logistics.common.dto.ApiResponse;
import com.app.logistics.common.validations.OnCreate;
import com.app.logistics.common.validations.OnUpdate;
import com.app.logistics.vehicle.dto.VehicleRequest;
import com.app.logistics.vehicle.dto.VehicleResponse;
import com.app.logistics.vehicle.service.VehicleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("logistic/vehicle")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping("/fetch")
    public ResponseEntity<ApiResponse<VehicleResponse>> fetchVehicle(@RequestParam Integer vehicleId) {
        VehicleResponse result = vehicleService.fetchVehicle(vehicleId);

        ApiResponse<VehicleResponse> apiResponse = new ApiResponse
                .Builder<VehicleResponse>(true, result)
                .message("Vehicle fetched successfully.")
                .timeStamp()
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/fetchall")
    public ResponseEntity<ApiResponse<List<VehicleResponse>>> fetchAllVehicle(
            @RequestParam Integer operatorId,
            @RequestParam(defaultValue = "1") int pageNo) {

        List<VehicleResponse> result = vehicleService.fetchAllVehicle(operatorId, pageNo);

        ApiResponse<List<VehicleResponse>> apiResponse = new ApiResponse
                .Builder<List<VehicleResponse>>(true, result)
                .message("Vehicle list fetched successfully.")
                .timeStamp()
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<ApiResponse<VehicleResponse>> saveVehicle(
            @Validated(OnCreate.class) @RequestBody VehicleRequest vehicleRequest,
            @AuthenticationPrincipal AuthDetails authDetails) {

        VehicleResponse result = vehicleService.saveVehicle(vehicleRequest, authDetails);

        ApiResponse<VehicleResponse> apiResponse = new ApiResponse
                .Builder<VehicleResponse>(true, result)
                .message("Vehicle created successfully.")
                .timeStamp()
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<VehicleResponse>> updateVehicle(
            @Validated(OnUpdate.class) @RequestBody VehicleRequest vehicleRequest,
            @AuthenticationPrincipal AuthDetails authDetails) {

        VehicleResponse result = vehicleService.updateVehicle(vehicleRequest, authDetails);

        ApiResponse<VehicleResponse> apiResponse = new ApiResponse
                .Builder<VehicleResponse>(true, result)
                .message("Vehicle updated successfully.")
                .timeStamp()
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{vehicleId}")
    public ResponseEntity<ApiResponse<Void>> deleteVehicle(@PathVariable Integer vehicleId) {
        vehicleService.deleteVehicle(vehicleId);

        ApiResponse<Void> apiResponse = new ApiResponse
                .Builder<Void>(true, null)
                .message("Vehicle deleted successfully.")
                .timeStamp()
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/fetchByNumber")
    public ResponseEntity<ApiResponse<VehicleResponse>> fetchByVehicleNumber(@RequestParam String vehicleNumber) {
        VehicleResponse result = vehicleService.fetchByVehicleNumber(vehicleNumber);

        ApiResponse<VehicleResponse> apiResponse = new ApiResponse
                .Builder<VehicleResponse>(true, result)
                .message("Vehicle fetched successfully.")
                .timeStamp()
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}