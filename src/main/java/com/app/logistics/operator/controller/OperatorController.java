package com.app.logistics.operator.controller;

import com.app.logistics.dto.MessageDTO.ResponseMessageDTO;
import com.app.logistics.dto.Operator.RQTOperatorDTO;
import com.app.logistics.dto.Operator.RSPOperatorDTO;
import com.app.logistics.common.dto.OperatorRequest;
import com.app.logistics.operator.service.OperatorService;
import com.app.logistics.authUtils.CustomizedUserDetails;
import com.app.logistics.common.validations.OnCreate;
import com.app.logistics.common.validations.OnUpdate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    public OperatorRequest fetchCarrierOptions(){
        return operatorService.fetchCarrierOption();
    }


}
