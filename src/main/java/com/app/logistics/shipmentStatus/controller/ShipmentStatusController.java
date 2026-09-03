package com.app.logistics.shipmentStatus.controller;

import com.app.logistics.auth.authUtils.AuthDetails;
import com.app.logistics.common.dto.ApiResponse;
import com.app.logistics.common.validations.OnCreate;
import com.app.logistics.shipmentStatus.dto.ShipmentStatusResponse;
import com.app.logistics.shipmentStatus.dto.ShipmentStatusRequest;
import com.app.logistics.shipmentStatus.service.ShipmentStatusService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("logistic/status")
public class ShipmentStatusController {

    private final ShipmentStatusService shipmentStatusService;

    public ShipmentStatusController(ShipmentStatusService shipmentStatusService){
        this.shipmentStatusService = shipmentStatusService;
    }

    @GetMapping("/fetch")
    public ResponseEntity<ApiResponse<ShipmentStatusResponse>> fetchSpecificStatus(@RequestParam Integer shippingStatusId){
        ShipmentStatusResponse result = shipmentStatusService.fetchStatus(shippingStatusId);
        return ResponseEntity.ok(new ApiResponse.Builder<>(true, result)
                .message("Shipment status fetched successfully")
                .timeStamp()
                .build());
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<ShipmentStatusResponse>> updateStatus(@Validated(OnCreate.class) @RequestBody ShipmentStatusRequest shipmentStatusRequest,
                                                                            @AuthenticationPrincipal AuthDetails userDetails){
        ShipmentStatusResponse result = shipmentStatusService.updateStatus(shipmentStatusRequest, userDetails);
        return ResponseEntity.ok(new ApiResponse.Builder<>(true, result)
                .message("Shipment status updated successfully")
                .timeStamp()
                .build());
    }
}