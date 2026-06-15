package com.app.projectlogistics.Controller;
import com.app.projectlogistics.DataTransferObject.Driver.RQTDriverDTO;
import com.app.projectlogistics.DataTransferObject.Driver.RSPDriverDTO;
import com.app.projectlogistics.DataTransferObject.MessageDTO.ResponseMessageDTO;
import com.app.projectlogistics.Service.DriverService;
import com.app.projectlogistics.UserDetailServices.CustomizedUserDetails;
import com.app.projectlogistics.validationInterface.OnUpdate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("logistic/driver")
public class DriverController {

    private final DriverService driverService;

    public DriverController(DriverService driverService){
        this.driverService = driverService;
    }

    @GetMapping("/fetch")
    @ResponseBody
    public ResponseEntity<RSPDriverDTO> fetchDriver(@RequestParam Integer driverId){
        RSPDriverDTO result = driverService.fetchDriver(driverId);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.noContent().build();
    }

    @GetMapping("/fetchall")
    @ResponseBody
    public ResponseEntity<ResponseMessageDTO> fetchAllDriver(@RequestParam Integer operatorId, @RequestParam(defaultValue = "1") int pageNo){
        ResponseMessageDTO result = driverService.fetchAllDriver(operatorId,pageNo);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.noContent().build();
    }

    @PostMapping("/save")
    @ResponseBody
    public ResponseEntity<String> saveDriver(@RequestBody Map<String,Object> driverDetails, @AuthenticationPrincipal CustomizedUserDetails userDetails){

        RQTDriverDTO rqtDriverDTO = new RQTDriverDTO();
        rqtDriverDTO.setDriverName(driverDetails.get("driverName").toString());
        rqtDriverDTO.setDriverPhoneNo(driverDetails.get("driverPhoneNo").toString());
        rqtDriverDTO.setDriverLicenseNo(driverDetails.get("driverLicenseNo").toString());
        rqtDriverDTO.setOperatorId(Integer.parseInt(driverDetails.get("operatorId").toString()));

        String username = driverDetails.get("accountUserName").toString();

        return driverService.saveDriver(rqtDriverDTO,username, userDetails);
    }

    @PutMapping("/update")
    @ResponseBody
    public RSPDriverDTO updateDriver(@Validated(OnUpdate.class) @RequestBody RQTDriverDTO rqtDriverDTO, @AuthenticationPrincipal CustomizedUserDetails userDetails){
        return driverService.updateDriver(rqtDriverDTO, userDetails);
    }

    @DeleteMapping("/delete/{operatorId}")
    @ResponseBody
    public ResponseEntity<String> deleteDriver(@PathVariable Integer operatorId){
        return driverService.deleteDriver(operatorId);
    }

    @GetMapping("/fetchByName")
    @ResponseBody
    public ResponseEntity<RSPDriverDTO> fetchByDriverName(@RequestParam String driverName) {
        RSPDriverDTO result = driverService.findByDriverName(driverName);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.noContent().build();
    }
}