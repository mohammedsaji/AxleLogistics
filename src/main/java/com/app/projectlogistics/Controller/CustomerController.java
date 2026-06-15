package com.app.projectlogistics.Controller;

import com.app.projectlogistics.DataTransferObject.Customer.RSPCustomerDTO;
import com.app.projectlogistics.Service.CustomerService;
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
    public ResponseEntity<RSPCustomerDTO> fetchCustomer(@RequestParam Integer customerId) {
        RSPCustomerDTO result = customerService.fetchCustomer(customerId);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.noContent().build();
    }
}