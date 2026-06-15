package com.app.projectlogistics.Service;

import com.app.projectlogistics.DataTransferObject.Driver.RSPDriverDTO;
import com.app.projectlogistics.DataTransferObject.Employee.RQTEmployeeDTO;
import com.app.projectlogistics.DataTransferObject.Employee.RSPEmployeeDTO;
import com.app.projectlogistics.DataTransferObject.MessageDTO.ResponseMessageDTO;
import com.app.projectlogistics.Repository.EmployeeRepo;
import com.app.projectlogistics.UserDetailServices.CustomizedUserDetails;
import com.app.projectlogistics.ValueObject.AccountVO;
import com.app.projectlogistics.ValueObject.EmployeeVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepo employeeRepo;

    private final AuthService authService;

    public EmployeeService(EmployeeRepo employeeRepo,
                           AuthService authService){
        this.employeeRepo = employeeRepo;
        this.authService = authService;
    }

    @Transactional(readOnly = true)
    public RSPEmployeeDTO fetchEmployee(Integer employeeId){
        EmployeeVO fetchEmployeeVO = employeeRepo.findById(employeeId).orElse(null);
        return voToDTOConverter(fetchEmployeeVO);
    }

    @Transactional(readOnly = true)
    public ResponseMessageDTO fetchAllEmployee(int pageNo) {
        if (pageNo < 1) {
            pageNo = 1;
        }
        int elementCount = 25;
        Pageable pageable = PageRequest.of(pageNo-1, elementCount, Sort.by("employeeId"));
        Page<EmployeeVO> page = employeeRepo.findAll(pageable);
        List<EmployeeVO> employeeVOList = page.getContent();
        List<RSPEmployeeDTO> rspEmployeeDTOList = new ArrayList<>();

        for (EmployeeVO employeeVO : employeeVOList) {
            rspEmployeeDTOList.add(voToDTOConverter(employeeVO));
        }
        ResponseMessageDTO responseMessageDTO = new ResponseMessageDTO();
        responseMessageDTO.setStatusCode(200);
        responseMessageDTO.setValue("EmployeeList",rspEmployeeDTOList);
        responseMessageDTO.setValue("TotalPages",page.getTotalPages());
        responseMessageDTO.setValue("TotalElements",page.getTotalElements());
        return responseMessageDTO;
    }

    public RSPEmployeeDTO voToDTOConverter(EmployeeVO employeeVO) {
        if (employeeVO == null) {
            return new RSPEmployeeDTO();
        }
        RSPEmployeeDTO rspEmployeeDTO = new RSPEmployeeDTO();

        rspEmployeeDTO.setEmployeeId(employeeVO.getEmployeeId());
        rspEmployeeDTO.setEmployeeName(employeeVO.getEmployeeName());
        rspEmployeeDTO.setEmployeePhoneNo(employeeVO.getEmployeePhoneNo());
        rspEmployeeDTO.setEmployeeDepartment(employeeVO.getEmployeeDepartment());
        rspEmployeeDTO.setEmployeeJoiningDate(employeeVO.getEmployeeJoiningDate());
        rspEmployeeDTO.setEmployeeStatus(employeeVO.getEmployeeStatus());
        rspEmployeeDTO.setReportingManagerId(employeeVO.getReportingManagerId());
        rspEmployeeDTO.setAccountId(employeeVO.getAccountVO().getAccountId());
        rspEmployeeDTO.setAccountVO(employeeVO.getAccountVO());
        rspEmployeeDTO.setCreatedAt(employeeVO.getCreatedAt());
        rspEmployeeDTO.setUpdatedAt(employeeVO.getUpdatedAt());

        return rspEmployeeDTO;
    }


    public EmployeeVO dtoToVOConverter(String action, RQTEmployeeDTO rqtEmployeeDTO, CustomizedUserDetails userDetails){
        if (action == null || rqtEmployeeDTO == null || userDetails == null) {
            return new EmployeeVO();
        }
        EmployeeVO employeeVO = new EmployeeVO();
        if(action.equalsIgnoreCase("SAVE")){
            employeeVO.setEmployeeId(rqtEmployeeDTO.getEmployeeId());
        }
        employeeVO.setEmployeeName(rqtEmployeeDTO.getEmployeeName());
        employeeVO.setEmployeePhoneNo(rqtEmployeeDTO.getEmployeePhoneNo());
        employeeVO.setEmployeeDepartment(rqtEmployeeDTO.getEmployeeDepartment());
        employeeVO.setEmployeeStatus(rqtEmployeeDTO.getEmployeeStatus());
        employeeVO.setReportingManagerId(rqtEmployeeDTO.getReportingManagerId());
        if(action.equalsIgnoreCase("SAVE")){
            employeeVO.setEmployeeJoiningDate(LocalDateTime.now());
            employeeVO.setCreatedAt(LocalDateTime.now());
        }else{
            employeeVO.setCreatedAt(rqtEmployeeDTO.getCreatedAt());
        }
        employeeVO.setUpdatedAt(LocalDateTime.now());
        employeeVO.setUpdatedBy(userDetails.getEmployeeId());

        return employeeVO;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public ResponseEntity<String> saveEmployee(RQTEmployeeDTO rqtEmployeeDTO, String username, CustomizedUserDetails userDetails){
        if (rqtEmployeeDTO == null || username == null || userDetails == null) {
            return ResponseEntity.status(400).body("Required metadata or payload context is missing");
        }
        AccountVO savedAccountVO = authService.saveUser(username);
        if(savedAccountVO != null){
            EmployeeVO savingEmployeeVO = dtoToVOConverter("SAVE",rqtEmployeeDTO,userDetails);
            savingEmployeeVO.setAccountVO(savedAccountVO);
            EmployeeVO savedEmployeeVO = employeeRepo.save(savingEmployeeVO);
            if(savedEmployeeVO.getEmployeeId() != null && savedEmployeeVO.getEmployeeId() > 0){
                return ResponseEntity.status(200).body("Account created Successfully.");
            }else{
                return ResponseEntity.status(400).body("Invalid or Bad Request");
            }
        }
        return ResponseEntity.status(500).body("Internal server error");
    }

    @Transactional(readOnly = true)
    public RSPEmployeeDTO fetchByEmployeeName(String employeeName) {
        if (employeeName == null || employeeName.trim().isEmpty()) {
            return null;
        }

        EmployeeVO fetchEmployeeVO = employeeRepo.findByEmployeeName(employeeName);
        if (fetchEmployeeVO != null) {
            return voToDTOConverter(fetchEmployeeVO);
        }

        return null;
    }
}
