package com.app.logistics.customer.service;

import com.app.logistics.customer.dto.CustomerRequest;
import com.app.logistics.customer.dto.CustomerResponse;
import com.app.logistics.customer.repo.CustomerRepo;
import com.app.logistics.customer.entity.Customer;
import com.app.logistics.customer.utils.CustomerMapper;
import com.app.logistics.common.exception.APIException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerService {

    private final CustomerRepo customerRepo;
    private final CustomerMapper customerMapper;

    public CustomerService(CustomerRepo customerRepo, CustomerMapper customerMapper) {
        this.customerRepo = customerRepo;
        this.customerMapper = customerMapper;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Customer saveCustomer(CustomerRequest customerRequest) {
        if (customerRequest == null) {
            throw new APIException("Customer request data payload cannot be null", HttpStatus.BAD_REQUEST);
        }
        Customer savingCustomer = customerMapper.toVO(customerRequest);
        return customerRepo.save(savingCustomer);
    }

    @Transactional(readOnly = true)
    public CustomerResponse fetchCustomer(Integer customerId) {
        if (customerId == null) {
            throw new APIException("Customer ID cannot be null", HttpStatus.BAD_REQUEST);
        }
        Customer customer = customerRepo.findById(customerId)
                .orElseThrow(() -> new APIException("Customer not found for ID: " + customerId, HttpStatus.NOT_FOUND));
        return customerMapper.toDTO(customer);
    }
}