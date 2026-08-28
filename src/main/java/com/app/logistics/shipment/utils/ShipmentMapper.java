package com.app.logistics.shipment.utils;

import com.app.logistics.common.utils.CommonMapper;
import com.app.logistics.shipment.dto.ShipmentRequest;
import com.app.logistics.shipment.dto.ShipmentResponse;
import com.app.logistics.shipment.entity.Shipment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ShipmentMapper extends CommonMapper<ShipmentRequest, ShipmentResponse, Shipment> {
}
