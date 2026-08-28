package com.app.logistics.shipmentStatus.controller;

import com.app.logistics.dto.ShipmentStatus.RQTShipmentStatusDTO;
import com.app.logistics.dto.ShipmentStatus.RSPShipmentStatusDTO;
import com.app.logistics.shipmentStatus.service.ShipmentStatusService;
import com.app.logistics.authUtils.CustomizedUserDetails;
import com.app.logistics.common.validations.OnCreate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("logistic/status")
public class ShipmentStatusController {

    private final ShipmentStatusService shipmentStatusService;

    public ShipmentStatusController(ShipmentStatusService shipmentStatusService){
        this.shipmentStatusService = shipmentStatusService;
    }

    @GetMapping("/fetch")
    @ResponseBody
    public ResponseEntity<RSPShipmentStatusDTO> fetchSpecificStatus(@RequestBody RQTShipmentStatusDTO rqtShipmentStatusDTO){
        RSPShipmentStatusDTO result = shipmentStatusService.fetchStatus(rqtShipmentStatusDTO);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.noContent().build();
    }

    @PutMapping("/update")
    @ResponseBody
    public RSPShipmentStatusDTO updateStatus(@Validated(OnCreate.class) @RequestBody RQTShipmentStatusDTO rqtShipmentStatusDTO, @AuthenticationPrincipal CustomizedUserDetails userDetails){
        return shipmentStatusService.updateStatus(rqtShipmentStatusDTO,userDetails);
    }
}