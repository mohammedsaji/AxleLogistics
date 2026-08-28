package com.app.logistics.common.controller;

import com.app.logistics.common.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("bop/common")
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> health(){

        ApiResponse<String> apiResponse = new ApiResponse
                .Builder<String>(true,"UP")
                .message("Backend Operations Platform is running")
                .timeStamp()
                .build();

        return new ResponseEntity<>(apiResponse,HttpStatus.OK);
    }


}
