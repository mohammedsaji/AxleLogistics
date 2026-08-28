package com.app.logistics.cargo.controller;

import com.app.logistics.cargo.dto.CargoResponse;
import com.app.logistics.cargo.service.CargoService;
import com.app.logistics.common.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("logistic/cargo")
public class CargoController {

    private final CargoService cargoService;

    public CargoController(CargoService cargoService) {
        this.cargoService = cargoService;
    }

    @GetMapping("/fetch")
    public ResponseEntity<ApiResponse<CargoResponse>> fetchCargo(@RequestParam Integer cargoId) {
        CargoResponse result = cargoService.fetchCargo(cargoId);

        ApiResponse<CargoResponse> apiResponse = new ApiResponse
                .Builder<CargoResponse>(true, result)
                .message("Cargo fetched successfully.")
                .timeStamp()
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}