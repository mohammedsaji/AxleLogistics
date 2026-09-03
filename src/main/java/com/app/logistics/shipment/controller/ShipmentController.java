package com.app.logistics.shipment.controller;

import com.app.logistics.auth.authUtils.AuthDetails;
import com.app.logistics.common.dto.ApiResponse;
import com.app.logistics.common.validations.OnCreate;
import com.app.logistics.common.validations.OnUpdate;
import com.app.logistics.shipment.dto.Composite.ShipmentSaveRequest;
import com.app.logistics.shipment.dto.ShipmentResponse;
import com.app.logistics.shipment.service.ShipmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("logistic/shipment")
public class ShipmentController {

    private final ShipmentService shipmentService;

    public ShipmentController(ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    @GetMapping("/fetchall")
    public ResponseEntity<ApiResponse<List<ShipmentResponse>>> fetchAllShipment(@RequestParam int pageNo) {
        List<ShipmentResponse> result = shipmentService.fetchAllShipment(pageNo);
        return ResponseEntity.ok(new ApiResponse.Builder<>(true, result)
                .message("Shipments fetched successfully")
                .timeStamp()
                .build());
    }

    @PostMapping("/save")
    public ResponseEntity<ApiResponse<ShipmentResponse>> saveShipment(@Validated(OnCreate.class) @RequestBody ShipmentSaveRequest shipmentSaveRequest,
                                                                      @AuthenticationPrincipal AuthDetails userDetails) {
        ShipmentResponse result = shipmentService.saveShipment(shipmentSaveRequest, userDetails);
        return ResponseEntity.ok(new ApiResponse.Builder<>(true, result)
                .message("Shipment created successfully")
                .timeStamp()
                .build());
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<ShipmentResponse>> updateShipment(@Validated(OnUpdate.class) @RequestBody ShipmentSaveRequest shipmentSaveRequest,
                                                                        @AuthenticationPrincipal AuthDetails userDetails) {
        ShipmentResponse result = shipmentService.updateShipment(shipmentSaveRequest, userDetails);
        return ResponseEntity.ok(new ApiResponse.Builder<>(true, result)
                .message("Shipment updated successfully")
                .timeStamp()
                .build());
    }

    @GetMapping("/fetch")
    public ResponseEntity<ApiResponse<ShipmentResponse>> fetchShipment(@RequestParam Integer shipmentId) {
        ShipmentResponse result = shipmentService.fetchShipment(shipmentId);
        return ResponseEntity.ok(new ApiResponse.Builder<>(true, result)
                .message("Shipment fetched successfully")
                .timeStamp()
                .build());
    }

    @GetMapping("/fetchbyid")
    public ResponseEntity<ApiResponse<ShipmentResponse>> fetchByShipmentId(@RequestParam Integer shipmentId) {
        ShipmentResponse result = shipmentService.fetchByShipmentId(shipmentId);
        return ResponseEntity.ok(new ApiResponse.Builder<>(true, result)
                .message("Shipment fetched successfully")
                .timeStamp()
                .build());
    }
}