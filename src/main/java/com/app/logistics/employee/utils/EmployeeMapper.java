package com.app.logistics.employee.utils;

import com.app.logistics.common.utils.CommonMapper;
import com.app.logistics.employee.dto.EmployeeRequest;
import com.app.logistics.employee.dto.EmployeeResponse;
import com.app.logistics.employee.entity.Employee;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmployeeMapper extends CommonMapper<EmployeeRequest, EmployeeResponse, Employee> {
}
