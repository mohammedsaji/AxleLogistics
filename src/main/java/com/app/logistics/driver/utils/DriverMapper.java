package com.app.logistics.driver.utils;

import com.app.logistics.common.utils.CommonMapper;
import com.app.logistics.driver.dto.DriverRequest;
import com.app.logistics.driver.dto.DriverResponse;
import com.app.logistics.driver.entity.Driver;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DriverMapper extends CommonMapper<DriverRequest, DriverResponse, Driver> {
    @Mapping(source = "operator.operatorId", target = "operatorId")
    DriverResponse toDTO(Driver driver);
}
