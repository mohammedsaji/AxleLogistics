package com.app.logistics.shipmentStatus.utils;

import com.app.logistics.common.utils.CommonMapper;
import com.app.logistics.shipmentStatus.dto.ShipmentStatusRequest;
import com.app.logistics.shipmentStatus.dto.ShipmentStatusResponse;
import com.app.logistics.shipmentStatus.entity.ShipmentStatus;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ShipmentStatusMapper extends CommonMapper<ShipmentStatusRequest, ShipmentStatusResponse, ShipmentStatus> {
}
