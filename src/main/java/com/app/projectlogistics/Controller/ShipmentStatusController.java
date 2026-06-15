package com.app.projectlogistics.Controller;

import com.app.projectlogistics.DataTransferObject.ShipmentStatus.RQTShipmentStatusDTO;
import com.app.projectlogistics.DataTransferObject.ShipmentStatus.RSPShipmentStatusDTO;
import com.app.projectlogistics.Service.ShipmentStatusService;
import com.app.projectlogistics.UserDetailServices.CustomizedUserDetails;
import com.app.projectlogistics.validationInterface.OnCreate;
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