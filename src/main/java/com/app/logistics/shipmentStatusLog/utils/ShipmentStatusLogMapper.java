package com.app.logistics.shipmentStatusLog.utils;

import com.app.logistics.common.utils.CommonMapper;
import com.app.logistics.shipmentStatusLog.dto.ShipmentStatusLogRequest;
import com.app.logistics.shipmentStatusLog.dto.ShipmentStatusLogResponse;
import com.app.logistics.shipmentStatusLog.entity.ShipmentStatusLog;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ShipmentStatusLogMapper extends CommonMapper<ShipmentStatusLogRequest, ShipmentStatusLogResponse, ShipmentStatusLog> {
}
