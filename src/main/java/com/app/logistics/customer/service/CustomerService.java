package com.app.logistics.customer.service;

import com.app.logistics.customer.dto.CustomerRequest;
import com.app.logistics.customer.dto.CustomerResponse;
import com.app.logistics.customer.repo.CustomerRepo;
import com.app.logistics.customer.entity.Customer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service
public class CustomerService {

    private final CustomerRepo customerRepo;

    public CustomerService(CustomerRepo customerRepo) {
        this.customerRepo = customerRepo;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Customer saveCustomer(CustomerRequest customerRequest) {

        if (customerRequest == null) {
            throw new IllegalArgumentException("Customer request data payload cannot be null");
        }

        Customer savingCustomer = dtoToVOConverter(customerRequest);
        Customer savedCustomer = customerRepo.save(savingCustomer);
        return savedCustomer;
    }

    public Customer dtoToVOConverter(CustomerRequest customerRequest) {

        if (customerRequest == null) {
            return new Customer();
        }

        Customer customer = new Customer();
        customer.setCustomerId(customerRequest.getCustomerId());
        customer.setCustomerName(customerRequest.getCustomerName());
        customer.setCustomerEmail(customerRequest.getCustomerEmail());
        customer.setCustomerPhoneno(customerRequest.getCustomerPhoneno());
        customer.setCreatedAt(customerRequest.getCreatedAt());
        customer.setCreatedBy(customerRequest.getCreatedBy());

        return customer;
    }

    public CustomerResponse voToDTOConverter(Customer customer) {

        if (customer == null) {
            return new CustomerResponse();
        }

        CustomerResponse customerResponse = new CustomerResponse();
        customerResponse.setCustomerId(customer.getCustomerId());
        customerResponse.setCustomerName(customer.getCustomerName());
        customerResponse.setCustomerEmail(customer.getCustomerEmail());
        customerResponse.setCustomerPhoneno(customer.getCustomerPhoneno());
        customerResponse.setCreatedAt(customer.getCreatedAt());
        customerResponse.setCreatedBy(customer.getCreatedBy());
        return customerResponse;
    }

    @Transactional(readOnly = true)
    public CustomerResponse fetchCustomer(Integer customerId) {

        if (customerId == null) {
            return new CustomerResponse();
        }

        Customer fetchCustomer = customerRepo.findById(customerId).orElse(new Customer());
        return voToDTOConverter(fetchCustomer);
    }

//    @Transactional(readOnly = true)
//    public List<RSPCustomerDTO> fetchAllCustomer(int pageNo) {
//
//        if (pageNo < 1) {
//            pageNo = 1;
//        }
//
//        int elementCount = 25;
//        Pageable pageable = PageRequest.of(pageNo - 1, elementCount, Sort.by("customerId").ascending());
//        Page<CustomerVO> page = customerRepo.findAll(pageable);
//        List<CustomerVO> customerVOList = page.getContent();
//        List<RSPCustomerDTO> rspCustomerDTOList = new ArrayList<>();
//        for (CustomerVO customerVO : customerVOList) {
//            rspCustomerDTOList.add(voToDTOConverter(customerVO));
//        }
//        return rspCustomerDTOList;
//    }

//    public RSPCustomerDTO findCustomerByName(String customerName) {
//
//        if (customerName == null || customerName.trim().isEmpty()) {
//            return new RSPCustomerDTO();
//        }
//
//        CustomerVO getCustomerVO = customerRepo.findByCustomerName(customerName);
//        return voToDTOConverter(getCustomerVO);
//    }

}
