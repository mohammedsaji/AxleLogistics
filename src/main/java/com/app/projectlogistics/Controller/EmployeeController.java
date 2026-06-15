package com.app.projectlogistics.Controller;

import com.app.projectlogistics.DataTransferObject.Employee.RQTEmployeeDTO;
import com.app.projectlogistics.DataTransferObject.Employee.RSPEmployeeDTO;
import com.app.projectlogistics.DataTransferObject.MessageDTO.ResponseMessageDTO;
import com.app.projectlogistics.Service.EmployeeService;
import com.app.projectlogistics.UserDetailServices.CustomizedUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("logistic/employee")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService){
        this.employeeService = employeeService;
    }

    @GetMapping("/fetch")
    @ResponseBody
    public ResponseEntity<RSPEmployeeDTO> fetchEmployee(@RequestParam Integer employeeId){
        RSPEmployeeDTO result = employeeService.fetchEmployee(employeeId);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.noContent().build();
    }

    @GetMapping("/fetchall")
    @ResponseBody
    public ResponseMessageDTO fetchAllEmployee(@RequestParam int pageNo){
        return employeeService.fetchAllEmployee(pageNo);
    }

    @GetMapping("/fetchByName")
    @ResponseBody
    public ResponseEntity<RSPEmployeeDTO> fetchByEmployeeName(@RequestParam String employeeName) {
        RSPEmployeeDTO result = employeeService.fetchByEmployeeName(employeeName);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.noContent().build();
    }


    @PostMapping("/save")
    @ResponseBody
    public ResponseEntity<String> saveEmployee(@RequestBody Map<String,Object> employeeDetails, @AuthenticationPrincipal CustomizedUserDetails userDetails) throws IOException {

        RQTEmployeeDTO rqtEmployeeDTO = new RQTEmployeeDTO();
        rqtEmployeeDTO.setEmployeeName(employeeDetails.get("employeeName").toString());
        rqtEmployeeDTO.setEmployeePhoneNo(employeeDetails.get("employeePhoneNo").toString());
        rqtEmployeeDTO.setEmployeeDepartment(employeeDetails.get("employeeDepartment").toString());
        rqtEmployeeDTO.setReportingManagerId(Integer.valueOf(employeeDetails.get("reportingManagerId").toString()));
        rqtEmployeeDTO.setEmployeeStatus(employeeDetails.get("employeeStatus").toString());

        String username = employeeDetails.get("accountUserName").toString();

        return employeeService.saveEmployee(rqtEmployeeDTO,username,userDetails);
    }
}