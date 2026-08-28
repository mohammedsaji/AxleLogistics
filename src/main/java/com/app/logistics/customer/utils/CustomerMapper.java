package com.app.logistics.customer.utils;

import com.app.logistics.common.utils.CommonMapper;
import com.app.logistics.customer.dto.CustomerRequest;
import com.app.logistics.customer.dto.CustomerResponse;
import com.app.logistics.customer.entity.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper extends CommonMapper<CustomerRequest, CustomerResponse, Customer> {
}
