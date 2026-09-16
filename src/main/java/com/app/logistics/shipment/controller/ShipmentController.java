package com.app.logistics.shipment.controller;

import com.app.logistics.auth.authUtils.AuthDetails;
import com.app.logistics.common.dto.ApiResponse;
import com.app.logistics.common.exception.APIException;
import com.app.logistics.common.validations.OnUpdate;
import com.app.logistics.shipment.dto.Composite.ShipmentFetchResponse;
import com.app.logistics.shipment.dto.Composite.ShipmentSaveRequest;
import com.app.logistics.shipment.dto.Composite.ShipmentUpdate;
import com.app.logistics.shipment.dto.ShipmentResponse;
import com.app.logistics.shipment.dto.ShipmentTrackingResponse;
import com.app.logistics.shipment.service.ShipmentService;
import com.app.logistics.common.validations.OnShipmentSave;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("logistic/shipment")
public class ShipmentController {

    private final ShipmentService shipmentService;

    public ShipmentController(ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    @GetMapping("/fetchall")
    public ResponseEntity<ApiResponse<Map<String, Object>>> fetchAllShipment(@RequestParam int pageNo) {
        Map<String, Object> result = shipmentService.fetchAllShipment(pageNo);
        return ResponseEntity.ok(new ApiResponse.Builder<>(true, result)
                .message("Shipments fetched successfully")
                .timeStamp()
                .build());
    }

    /**
     * Driver-scoped: every active shipment assigned to the calling
     * driver's own account. Identity comes from the auth principal only.
     */
    @GetMapping("/fetchmine")
    public ResponseEntity<ApiResponse<List<ShipmentResponse>>> fetchMyActiveShipments(@AuthenticationPrincipal AuthDetails authDetails) {
        List<ShipmentResponse> result = shipmentService.fetchMyActiveShipments(authDetails);
        return ResponseEntity.ok(new ApiResponse.Builder<>(true, result)
                .message("Active shipments fetched successfully")
                .timeStamp()
                .build());
    }

    @PostMapping("/save")
    public ResponseEntity<ApiResponse<ShipmentResponse>> saveShipment(@Validated(OnShipmentSave.class) @RequestBody ShipmentSaveRequest shipmentSaveRequest,
                                                                      @AuthenticationPrincipal AuthDetails authDetails) {
        ShipmentResponse result = shipmentService.saveShipment(shipmentSaveRequest, authDetails);
        return ResponseEntity.ok(new ApiResponse.Builder<>(true, result)
                .message("Shipment created successfully")
                .timeStamp()
                .build());
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<String>> updateShipment(@Validated(OnUpdate.class) @RequestBody ShipmentUpdate shipmentUpdate,
                                                                        @AuthenticationPrincipal AuthDetails authDetails) {
        HttpStatus httpStatus = shipmentService.updateShipment(shipmentUpdate, authDetails);

        if(!httpStatus.is2xxSuccessful()){
            throw new APIException("Failed to update Operator for current shipment.", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(new ApiResponse.Builder<>(true, "Operator updated for shipment successfully.")
                .message("Shipment updated successfully")
                .timeStamp()
                .build());
    }

    // /update intentionally omitted for now — add back when a booking-field
    // edit (e.g. delivery-date reschedule) becomes an actual requirement.

    /**
     * Ownership-checked fetch: FEDERATE-DRIVER can only fetch a shipment
     * they are currently assigned to (enforced in the service, from
     * authDetails — never from a client-supplied id).
     */
    @GetMapping("/fetch")
    public ResponseEntity<ApiResponse<ShipmentFetchResponse>> fetchShipment(@RequestParam Integer shippingId,
                                                                            @AuthenticationPrincipal AuthDetails authDetails) {
        ShipmentFetchResponse result = shipmentService.fetchShipment(shippingId, authDetails);
        return ResponseEntity.ok(new ApiResponse.Builder<>(true, result)
                .message("Shipment fetched successfully")
                .timeStamp()
                .build());
    }

    @GetMapping("/tracking")
    public ResponseEntity<ApiResponse<ShipmentTrackingResponse>> trackShipment(
            @RequestParam String decodedTrackingId) {

        ShipmentTrackingResponse result =
                shipmentService.trackShipment(decodedTrackingId);

        return ResponseEntity.ok(
                new ApiResponse.Builder<>(true, result)
                        .message("Shipment tracking details fetched successfully")
                        .timeStamp()
                        .build()
        );
    }
}