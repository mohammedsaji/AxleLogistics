package com.app.logistics.cargo.utils;

import com.app.logistics.cargo.dto.CargoRequest;
import com.app.logistics.cargo.dto.CargoResponse;
import com.app.logistics.cargo.entity.Cargo;
import com.app.logistics.common.utils.CommonMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CargoMapper extends CommonMapper<CargoRequest, CargoResponse, Cargo> {
}
