package com.app.projectlogistics.Controller;

import com.app.projectlogistics.DataTransferObject.Composite.ShipmentSaveDTO;
import com.app.projectlogistics.DataTransferObject.MessageDTO.ResponseMessageDTO;
import com.app.projectlogistics.DataTransferObject.Shipment.RQTShipmentDTO;
import com.app.projectlogistics.DataTransferObject.Shipment.RSPShipmentDTO;
import com.app.projectlogistics.Service.ShipmentService;
import com.app.projectlogistics.UserDetailServices.CustomizedUserDetails;
import com.app.projectlogistics.validationInterface.OnCreate;
import com.app.projectlogistics.validationInterface.OnUpdate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("logistic/shipment")
public class ShipmentController {

    private final ShipmentService shipmentService;

    public ShipmentController(ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    @GetMapping("/fetchall")
    @ResponseBody
    public ResponseEntity<ResponseMessageDTO> fetchAllShipment(@RequestParam int pageNo){
        ResponseMessageDTO result = shipmentService.fetchAllShipment(pageNo);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.noContent().build();
    }

    @PostMapping("/save")
    @ResponseBody
    public RSPShipmentDTO saveShipment(@Validated(OnCreate.class) @RequestBody ShipmentSaveDTO shipmentSaveDTO, @AuthenticationPrincipal CustomizedUserDetails userDetails){
        return shipmentService.saveShipment(shipmentSaveDTO,userDetails);
    }

    @PutMapping("/update")
    @ResponseBody
    public RSPShipmentDTO updateShipment(@Validated(OnUpdate.class) @RequestBody ShipmentSaveDTO shipmentSaveDTO, @AuthenticationPrincipal CustomizedUserDetails userDetails){
        return shipmentService.updateShipment(shipmentSaveDTO,userDetails);
    }

    @GetMapping("/fetch")
    @ResponseBody
    public ResponseEntity<RSPShipmentDTO> fetchShipment(@RequestParam Integer shipmentId){
        RSPShipmentDTO result = shipmentService.fetchShipment(shipmentId);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.noContent().build();
    }

    @GetMapping("/fetchbyid")
    @ResponseBody
    public ResponseEntity<RSPShipmentDTO> fetchByShipmentId(@RequestParam Integer shipmentId){
        RSPShipmentDTO result = shipmentService.fetchByShipmentId(shipmentId);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.noContent().build();
    }
}