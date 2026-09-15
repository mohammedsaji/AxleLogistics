package com.app.logistics.employee.utils;

import com.app.logistics.common.utils.CommonMapper;
import com.app.logistics.employee.dto.EmployeeRequest;
import com.app.logistics.employee.dto.EmployeeResponse;
import com.app.logistics.employee.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface EmployeeMapper extends CommonMapper<EmployeeRequest, EmployeeResponse, Employee> {

    @Override
    @Mappings({
            @Mapping(source = "account.accountId", target = "accountId"),
            @Mapping(source = "account.accountUsername", target = "accountUserName"),
            @Mapping(source = "account.accountRole", target = "accountRole"),
            @Mapping(source = "account.accountStatus", target = "accountStatus"),
            @Mapping(source = "account.accountEmail", target = "accountEmail")
    })
    EmployeeResponse toDTO(Employee employee);
}