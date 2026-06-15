package com.app.projectlogistics.Service;

import com.app.projectlogistics.DataTransferObject.Customer.RSPCustomerDTO;
import com.app.projectlogistics.DataTransferObject.Driver.RQTDriverDTO;
import com.app.projectlogistics.DataTransferObject.Driver.RSPDriverDTO;
import com.app.projectlogistics.DataTransferObject.MessageDTO.ResponseMessageDTO;
import com.app.projectlogistics.Repository.DriverRepo;
import com.app.projectlogistics.UserDetailServices.CustomizedUserDetails;
import com.app.projectlogistics.ValueObject.AccountVO;
import com.app.projectlogistics.ValueObject.CustomerVO;
import com.app.projectlogistics.ValueObject.DriverVO;
import com.app.projectlogistics.ValueObject.OperatorVO;
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
public class DriverService {

    private final DriverRepo driverRepo;

    private final OperatorService operatorService;

    private final AuthService authService;

    public DriverService(DriverRepo driverRepo,
                         OperatorService operatorService,
                         AuthService authService){
        this.driverRepo = driverRepo;
        this.operatorService = operatorService;
        this.authService = authService;
    }

    public RSPDriverDTO fetchDriver(Integer driverId){
        if (driverId == null) {
            return new RSPDriverDTO();
        }
        DriverVO fetchedDriverVO = driverRepo.findById(driverId).orElse(new DriverVO());
        return voToDTOConverter(fetchedDriverVO);
    }

    public OperatorVO fetchOperatorVO(Integer operatorId){
        if (operatorId == null) {
            return new OperatorVO();
        }
        OperatorVO getOperatorVO = operatorService.internalFetchService(operatorId);
        return  getOperatorVO;
    }

    public DriverVO dtoToVOConverter(String action, RQTDriverDTO rqtDriverDTO, CustomizedUserDetails userDetails){
        if (action == null || rqtDriverDTO == null || userDetails == null) {
            return new DriverVO();
        }
        DriverVO driverVO = new DriverVO();
        if(action.equalsIgnoreCase("Update")){
            driverVO.setDriverId(rqtDriverDTO.getDriverId());
        }
        driverVO.setDriverName(rqtDriverDTO.getDriverName());
        driverVO.setDriverLicenseNo(rqtDriverDTO.getDriverLicenseNo());
        driverVO.setDriverPhoneNo(rqtDriverDTO.getDriverPhoneNo());
        driverVO.setOperatorVO(fetchOperatorVO(rqtDriverDTO.getOperatorId()));
        driverVO.setAccountVO(userDetails.getAccountVO());
        if(action.equalsIgnoreCase("Save")){
            driverVO.setCreatedAt(LocalDateTime.now());
        }else{
            driverVO.setCreatedAt(rqtDriverDTO.getCreatedAt());
        }
        driverVO.setUpdatedAt(LocalDateTime.now());
        driverVO.setUpdatedBy(userDetails.getEmployeeId());

        return driverVO;
    }

    public RSPDriverDTO voToDTOConverter(DriverVO driverVO){
        if (driverVO == null) {
            return new RSPDriverDTO();
        }
        RSPDriverDTO rspDriverDTO = new RSPDriverDTO();
        rspDriverDTO.setDriverId(driverVO.getDriverId());
        rspDriverDTO.setDriverName(driverVO.getDriverName());
        rspDriverDTO.setDriverPhoneNo(driverVO.getDriverPhoneNo());
        rspDriverDTO.setDriverLicenseNo(driverVO.getDriverLicenseNo());
        rspDriverDTO.setCreatedAt(driverVO.getCreatedAt());
        rspDriverDTO.setCreatedAt(driverVO.getCreatedAt());
        rspDriverDTO.setUpdatedBy(driverVO.getUpdatedBy());

        return rspDriverDTO;
    }

    @Transactional (propagation = Propagation.REQUIRED)
    public DriverVO internalFetchService(Integer driverId){
        if (driverId == null) {
            return new DriverVO();
        }
        DriverVO driverVO = driverRepo.findById(driverId).orElse(new DriverVO());
        return driverVO;
    }

    @Transactional(readOnly = true)
    public ResponseMessageDTO fetchAllDriver(Integer operatorId, int pageNo) {
        if (pageNo < 1) {
            pageNo = 1;
        }
        int elementCount = 25;
        Pageable pageable = PageRequest.of(pageNo-1,elementCount, Sort.by("driverId").ascending());
        Page<DriverVO> page = driverRepo.findByOperatorVO_OperatorId(operatorId,pageable);
        List<DriverVO> driverVOList = page.getContent();
        List<RSPDriverDTO> rspDriverDTOList = new ArrayList<>();
        for(DriverVO driverVO:driverVOList){
            rspDriverDTOList.add(voToDTOConverter(driverVO));
        }
        ResponseMessageDTO responseMessageDTO = new ResponseMessageDTO();
        responseMessageDTO.setStatusCode(200);
        responseMessageDTO.setValue("DriverList",rspDriverDTOList);
        responseMessageDTO.setValue("TotalPages",page.getTotalPages());
        responseMessageDTO.setValue("TotalElements",page.getTotalElements());
        return responseMessageDTO;
    }

    @Transactional (propagation = Propagation.REQUIRED)
    public ResponseEntity<String> saveDriver(RQTDriverDTO rqtDriverDTO, String username, CustomizedUserDetails userDetails) {
        if (rqtDriverDTO == null || username == null || userDetails == null) {
            return ResponseEntity.status(400).body("Required metadata context missing");
        }

        AccountVO savedAccountVO = authService.saveUser(username);

        if(savedAccountVO != null){
            DriverVO savingDriverVO = dtoToVOConverter("Save",rqtDriverDTO,userDetails);
            savingDriverVO.setAccountVO(savedAccountVO);

            OperatorVO getOperatorVO = operatorService.internalFetchService(rqtDriverDTO.getOperatorId());
            if(getOperatorVO != null && getOperatorVO.getOperatorId() > 0){
                savingDriverVO.setOperatorVO(getOperatorVO);
            }else{
                return ResponseEntity.status(400).body("Error: Operator ID " + rqtDriverDTO.getOperatorId() + " does not exist in the system.");
            }
            DriverVO savedDriverVO = driverRepo.save(savingDriverVO);

            if(savedDriverVO.getDriverId() != null && savedDriverVO.getDriverId() > 0){
                return ResponseEntity.status(200).body("Account created Successfully.");
            }else{
                return ResponseEntity.status(400).body("Invalid or Bad Request");
            }
        }

        return ResponseEntity.status(500).body("Internal server error");
    }

    @Transactional (propagation = Propagation.REQUIRED)
    public RSPDriverDTO updateDriver(RQTDriverDTO rqtDriverDTO, CustomizedUserDetails userDetails) {
        if (rqtDriverDTO == null) {
            return new RSPDriverDTO();
        }
        DriverVO mutatedDriverVO = dtoToVOConverter("Update",rqtDriverDTO,userDetails);
        DriverVO updatedDriveVO = driverRepo.save(mutatedDriverVO);
        return voToDTOConverter(updatedDriveVO);
    }

    @Transactional (propagation = Propagation.REQUIRED)
    public ResponseEntity<String> deleteDriver(Integer operatorId) {
        if (operatorId == null) {
            return ResponseEntity.status(400).body("Driver identifier parameter cannot be null");
        }
        driverRepo.deleteById(operatorId);
        return ResponseEntity.status(200).body("Driver deleted successfully.");
    }

    @Transactional(readOnly = true)
    public RSPDriverDTO findByDriverName(String driverName) {
        if (driverName == null || driverName.trim().isEmpty()) {
            return null;
        }

        DriverVO fetchDriverVO = driverRepo.findByDriverName(driverName);
        if(fetchDriverVO != null){
            return voToDTOConverter(fetchDriverVO);
        }
        return null;
    }
}
