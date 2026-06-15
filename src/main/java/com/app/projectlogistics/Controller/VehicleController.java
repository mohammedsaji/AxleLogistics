package com.app.projectlogistics.Controller;

import com.app.projectlogistics.DataTransferObject.MessageDTO.ResponseMessageDTO;
import com.app.projectlogistics.DataTransferObject.Vehicle.RQTVehicleDTO;
import com.app.projectlogistics.DataTransferObject.Vehicle.RSPVehicleDTO;
import com.app.projectlogistics.Service.VehicleService;
import com.app.projectlogistics.UserDetailServices.CustomizedUserDetails;
import com.app.projectlogistics.validationInterface.OnCreate;
import com.app.projectlogistics.validationInterface.OnUpdate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("logistic/vehicle")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService){
        this.vehicleService = vehicleService;
    }

    @GetMapping("/fetch")
    @ResponseBody
    public ResponseEntity<RSPVehicleDTO> fetchVehicle(@RequestParam Integer driverId){
        RSPVehicleDTO result = vehicleService.fetchVehicle(driverId);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.noContent().build();
    }

    @GetMapping("/fetchall")
    @ResponseBody
    public ResponseEntity<ResponseMessageDTO> fetchAllVehicle(@RequestParam Integer vehicleId, @RequestParam(defaultValue = "1") int pageNo){
        ResponseMessageDTO result = vehicleService.fetchAllVehicle(vehicleId,pageNo);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.noContent().build();
    }

    @PostMapping("/save")
    @ResponseBody
    public RSPVehicleDTO saveVehicle(@Validated(OnCreate.class) @RequestBody RQTVehicleDTO rqtVehicleDTO, @AuthenticationPrincipal CustomizedUserDetails userDetails){
        return vehicleService.saveVehicle(rqtVehicleDTO, userDetails);
    }

    @PutMapping("/update")
    @ResponseBody
    public RSPVehicleDTO updateVehicle(@Validated(OnUpdate.class) @RequestBody RQTVehicleDTO rqtVehicleDTO, @AuthenticationPrincipal CustomizedUserDetails userDetails){
        return vehicleService.updateVehicle(rqtVehicleDTO, userDetails);
    }

    @DeleteMapping("/delete/{vehicleId}")
    @ResponseBody
    public ResponseEntity<String> deleteVehicle(@PathVariable Integer vehicleId){
        return vehicleService.deleteVehicle(vehicleId);
    }

    @GetMapping("/fetchByNumber")
    @ResponseBody
    public ResponseEntity<RSPVehicleDTO> fetchByVehicleNumber(@RequestParam String vehicleNumber){
        RSPVehicleDTO result = vehicleService.fetchByVehicleNumber(vehicleNumber);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.noContent().build();
    }
}