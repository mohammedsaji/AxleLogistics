package com.app.logistics.vehicle.utils;

import com.app.logistics.common.utils.CommonMapper;
import com.app.logistics.vehicle.dto.VehicleRequest;
import com.app.logistics.vehicle.dto.VehicleResponse;
import com.app.logistics.vehicle.entity.Vehicle;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VehicleMapper extends CommonMapper<VehicleRequest, VehicleResponse, Vehicle> {
}
