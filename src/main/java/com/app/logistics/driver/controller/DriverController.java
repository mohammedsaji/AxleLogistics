package com.app.logistics.driver.controller;
import com.app.logistics.driver.dto.DriverRequest;
import com.app.logistics.driver.dto.DriverResponse;
import com.app.logistics.dto.MessageDTO.ResponseMessageDTO;
import com.app.logistics.driver.service.DriverService;
import com.app.logistics.authUtils.CustomizedUserDetails;
import com.app.logistics.common.validations.OnUpdate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<DriverResponse> fetchDriver(@RequestParam Integer driverId){
        DriverResponse result = driverService.fetchDriver(driverId);
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

        DriverRequest driverRequest = new DriverRequest();
        driverRequest.setDriverName(driverDetails.get("driverName").toString());
        driverRequest.setDriverPhoneNo(driverDetails.get("driverPhoneNo").toString());
        driverRequest.setDriverLicenseNo(driverDetails.get("driverLicenseNo").toString());
        driverRequest.setOperatorId(Integer.parseInt(driverDetails.get("operatorId").toString()));

        String username = driverDetails.get("accountUserName").toString();

        return driverService.saveDriver(driverRequest,username, userDetails);
    }

    @PutMapping("/update")
    @ResponseBody
    public DriverResponse updateDriver(@Validated(OnUpdate.class) @RequestBody DriverRequest driverRequest, @AuthenticationPrincipal CustomizedUserDetails userDetails){
        return driverService.updateDriver(driverRequest, userDetails);
    }

    @DeleteMapping("/delete/{operatorId}")
    @ResponseBody
    public ResponseEntity<String> deleteDriver(@PathVariable Integer operatorId){
        return driverService.deleteDriver(operatorId);
    }

    @GetMapping("/fetchByName")
    @ResponseBody
    public ResponseEntity<DriverResponse> fetchByDriverName(@RequestParam String driverName) {
        DriverResponse result = driverService.findByDriverName(driverName);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.noContent().build();
    }
}