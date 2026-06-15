package com.app.projectlogistics.Controller;

import com.app.projectlogistics.DataTransferObject.MessageDTO.ResponseMessageDTO;
import com.app.projectlogistics.DataTransferObject.Operator.RQTOperatorDTO;
import com.app.projectlogistics.DataTransferObject.Operator.RSPOperatorDTO;
import com.app.projectlogistics.DataTransferObject.Utilities.CarrierOptionDTO;
import com.app.projectlogistics.Service.OperatorService;
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
@RequestMapping("logistic/operator")
public class  OperatorController {

    private final OperatorService operatorService;

    public OperatorController(OperatorService operatorService){
        this.operatorService = operatorService;
    }

    @GetMapping("/fetch")
    @ResponseBody
    public ResponseEntity<RSPOperatorDTO> fetchOperator(@RequestParam Integer operatorId){
        RSPOperatorDTO result = operatorService.fetchOperator(operatorId);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.noContent().build();
    }

    @GetMapping("/fetchall")
    @ResponseBody
    public ResponseEntity<ResponseMessageDTO> fetchAllOperator(@RequestParam String operatorTransportType, @RequestParam(defaultValue = "1") int pageNo){
        System.out.println("clicked operator type " + operatorTransportType);
        ResponseMessageDTO result = operatorService.fetchAllOperator(operatorTransportType, pageNo);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.noContent().build();
    }

    @GetMapping("/fetchByName")
    public ResponseEntity<RSPOperatorDTO> getOperatorByName(@RequestParam String operatorName) {
        RSPOperatorDTO result = operatorService.fetchByOperatorName(operatorName);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.noContent().build();
    }

    @PostMapping("/save")
    @ResponseBody
    public RSPOperatorDTO saveOperator(@Validated(OnCreate.class) @RequestBody RQTOperatorDTO rqtOperatorDTO, @AuthenticationPrincipal CustomizedUserDetails userDetails){
        return operatorService.saveOperator(rqtOperatorDTO, userDetails);
    }

    @PutMapping("/update")
    @ResponseBody
    public RSPOperatorDTO updateOperator(@Validated(OnUpdate.class) @RequestBody RQTOperatorDTO rqtOperatorDTO, @AuthenticationPrincipal CustomizedUserDetails userDetails){
        return operatorService.updateOperator(rqtOperatorDTO, userDetails);
    }

    @DeleteMapping("/delete/{operatorId}")
    @ResponseBody
    public ResponseEntity<String> deleteOperator(@PathVariable Integer operatorId){
        return operatorService.deleteOperator(operatorId);
    }

    @GetMapping("/plans")
    @ResponseBody
    public CarrierOptionDTO fetchCarrierOptions(){
        return operatorService.fetchCarrierOption();
    }


}
