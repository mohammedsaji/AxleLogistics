package com.app.logistics.customer.controller;

import com.app.logistics.customer.dto.CustomerResponse;
import com.app.logistics.customer.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("logistic/customer")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/fetch")
    @ResponseBody
    public ResponseEntity<CustomerResponse> fetchCustomer(@RequestParam Integer customerId) {
        CustomerResponse result = customerService.fetchCustomer(customerId);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.noContent().build();
    }
}