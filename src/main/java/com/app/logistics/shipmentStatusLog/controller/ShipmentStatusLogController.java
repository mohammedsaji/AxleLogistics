package com.app.logistics.shipmentStatusLog.controller;

import com.app.logistics.auth.authUtils.AuthDetails;
import com.app.logistics.common.dto.ApiResponse;
import com.app.logistics.shipmentStatusLog.dto.ShipmentStatusLogRequest;
import com.app.logistics.shipmentStatusLog.dto.ShipmentStatusLogResponse;
import com.app.logistics.shipmentStatusLog.service.ShipmentStatusLogService;
import com.app.logistics.shipmentStatusLog.utils.OnDependentSave;
import com.app.logistics.shipmentStatusLog.utils.OnOperatorSave;
import com.app.logistics.shipmentStatusLog.utils.OnStatusSave;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("logistic/shipmentLog")
public class ShipmentStatusLogController {

    private final ShipmentStatusLogService shipmentStatusService;

    public ShipmentStatusLogController(ShipmentStatusLogService shipmentStatusService) {
        this.shipmentStatusService = shipmentStatusService;
    }

    @GetMapping("/fetch")
    public ResponseEntity<ApiResponse<ShipmentStatusLogResponse>> fetchCurrentStatus(@RequestParam Integer shippingId,
                                                                                     @AuthenticationPrincipal AuthDetails authDetails) {
        ShipmentStatusLogResponse result = shipmentStatusService.fetchCurrentStatus(shippingId);
        return ResponseEntity.ok(new ApiResponse.Builder<>(true, result)
                .message("Shipment status fetched successfully")
                .timeStamp()
                .build());
    }

    @PostMapping("/dependent/save")
    public ResponseEntity<ApiResponse<ShipmentStatusLogResponse>> dependentSave(@Validated(OnDependentSave.class) @RequestBody ShipmentStatusLogRequest shipmentStatusLogRequest,
                                                                                @AuthenticationPrincipal AuthDetails authDetails) {
        ShipmentStatusLogResponse result = shipmentStatusService.dependentSave(shipmentStatusLogRequest, authDetails);
        return ResponseEntity.ok(new ApiResponse.Builder<>(true, result)
                .message("Vehicle, driver saved for shipment successfully")
                .timeStamp()
                .build());
    }

    @PostMapping("/status/save")
    public ResponseEntity<ApiResponse<ShipmentStatusLogResponse>> statusSave(@Validated({OnStatusSave.class, }) @RequestBody ShipmentStatusLogRequest shipmentStatusLogRequest,
                                                                             @AuthenticationPrincipal AuthDetails authDetails) {
        ShipmentStatusLogResponse result = shipmentStatusService.statusSave(shipmentStatusLogRequest, authDetails);
        return ResponseEntity.ok(new ApiResponse.Builder<>(true, result)
                .message("Shipment status saved successfully")
                .timeStamp()
                .build());
    }
}