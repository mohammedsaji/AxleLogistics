package com.app.projectlogistics.Controller;

import com.app.projectlogistics.DataTransferObject.Manager.RQTManagerDTO;
import com.app.projectlogistics.DataTransferObject.Manager.RSPManagerDTO;
import com.app.projectlogistics.DataTransferObject.MessageDTO.ResponseMessageDTO;
import com.app.projectlogistics.Service.ManagerService;
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
@RequestMapping("logistic/manager")
public class ManagerController {

    private final ManagerService managerService;

    public ManagerController(ManagerService managerService){
        this.managerService = managerService;
    }

    @GetMapping("/fetch")
    public ResponseEntity<RSPManagerDTO> fetchManager(@RequestParam Integer managerId){
        RSPManagerDTO result = managerService.fetchManager(managerId);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.noContent().build();
    }

    @GetMapping("/fetchall")
    public ResponseEntity<ResponseMessageDTO> fetchAllManager(@RequestParam Integer operatorId, @RequestParam(defaultValue = "1") int pageNo){
        ResponseMessageDTO result =  managerService.fetchAllManager(operatorId, pageNo);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.noContent().build();
    }

    @GetMapping("/fetchByName")
    public ResponseEntity<RSPManagerDTO> getManagerByName(@RequestParam String managerName) {
        RSPManagerDTO result = managerService.fetchByManagerName(managerName);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.noContent().build();
    }

    @PostMapping("/save")
    @ResponseBody
    public ResponseEntity<String> saveManager(@RequestBody Map<String,Object> managerDetails, @AuthenticationPrincipal CustomizedUserDetails userDetails){

        RQTManagerDTO rqtManagerDTO = new RQTManagerDTO();
        rqtManagerDTO.setManagerName(managerDetails.get("managerName").toString());
        rqtManagerDTO.setManagerContactNo(managerDetails.get("managerContactNo").toString());
        rqtManagerDTO.setManagerStatus(managerDetails.get("managerStatus").toString());
        rqtManagerDTO.setOperatorId(Integer.parseInt(managerDetails.get("operatorId").toString()));

        String username = managerDetails.get("accountUserName").toString();

        System.out.println(" manager name : "+rqtManagerDTO.getManagerName());
        System.out.println(" manager contact no : "+rqtManagerDTO.getManagerContactNo());
        System.out.println(" manager status : "+rqtManagerDTO.getManagerStatus());
        System.out.println(" operator id for manager : "+rqtManagerDTO.getOperatorId());

        return managerService.saveManager(rqtManagerDTO, username, userDetails);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteManager(@RequestBody RQTManagerDTO rqtManagerDTO){
        return managerService.deleteManager(rqtManagerDTO);
    }

    @PutMapping("/update")
    public RSPManagerDTO updateManager(@Validated(OnUpdate.class) @RequestBody RQTManagerDTO rqtManagerDTO, @AuthenticationPrincipal CustomizedUserDetails userDetails){
        return managerService.updateManager(rqtManagerDTO, userDetails);
    }
}
