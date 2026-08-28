package com.app.logistics.customer.controller;

import com.app.logistics.customer.dto.CustomerResponse;
import com.app.logistics.customer.service.CustomerService;
import com.app.logistics.common.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("logistic/customer")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/fetch")
    public ResponseEntity<ApiResponse<CustomerResponse>> fetchCustomer(@RequestParam Integer customerId) {
        CustomerResponse result = customerService.fetchCustomer(customerId);

        ApiResponse<CustomerResponse> apiResponse = new ApiResponse
                .Builder<CustomerResponse>(true, result)
                .message("Customer fetched successfully.")
                .timeStamp()
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}