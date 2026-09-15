package com.app.logistics.shipmentStatusLog.utils;

import com.app.logistics.common.utils.CommonMapper;
import com.app.logistics.shipmentStatusLog.dto.ShipmentStatusLogRequest;
import com.app.logistics.shipmentStatusLog.dto.ShipmentStatusLogResponse;
import com.app.logistics.shipmentStatusLog.entity.ShipmentStatusLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface ShipmentStatusLogMapper extends CommonMapper<ShipmentStatusLogRequest, ShipmentStatusLogResponse, ShipmentStatusLog> {

    @Mappings({
            @Mapping(source = "operator.operatorId", target = "operatorId"),
            @Mapping(source = "operator.operatorName", target = "operatorName"),
            @Mapping(source = "driver.driverId", target = "driverId"),
            @Mapping(source = "driver.driverName", target = "driverName"),
            @Mapping(source = "vehicle.vehicleId", target = "vehicleId"),
            @Mapping(source = "vehicle.vehicleNumber", target = "vehicleNumber"),
            @Mapping(source = "shipment.shippingId", target = "shippingId"),

    })
    ShipmentStatusLogResponse toDTO(ShipmentStatusLog shipmentStatusLog);
}
