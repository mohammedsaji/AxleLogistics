package com.app.logistics.driver.service;

import com.app.logistics.auth.service.AuthService;
import com.app.logistics.driver.dto.DriverRequest;
import com.app.logistics.driver.dto.DriverResponse;
import com.app.logistics.dto.MessageDTO.ResponseMessageDTO;
import com.app.logistics.driver.repo.DriverRepo;
import com.app.logistics.authUtils.CustomizedUserDetails;
import com.app.logistics.auth.entity.Auth;
import com.app.logistics.driver.entity.Driver;
import com.app.logistics.operator.entity.Operator;
import com.app.logistics.operator.service.OperatorService;
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

    public DriverResponse fetchDriver(Integer driverId){
        if (driverId == null) {
            return new DriverResponse();
        }
        Driver fetchedDriver = driverRepo.findById(driverId).orElse(new Driver());
        return voToDTOConverter(fetchedDriver);
    }

    public Operator fetchOperatorVO(Integer operatorId){
        if (operatorId == null) {
            return new Operator();
        }
        Operator getOperator = operatorService.internalFetchService(operatorId);
        return getOperator;
    }

    public Driver dtoToVOConverter(String action, DriverRequest driverRequest, CustomizedUserDetails userDetails){
        if (action == null || driverRequest == null || userDetails == null) {
            return new Driver();
        }
        Driver driver = new Driver();
        if(action.equalsIgnoreCase("Update")){
            driver.setDriverId(driverRequest.getDriverId());
        }
        driver.setDriverName(driverRequest.getDriverName());
        driver.setDriverLicenseNo(driverRequest.getDriverLicenseNo());
        driver.setDriverPhoneNo(driverRequest.getDriverPhoneNo());
        driver.setOperatorVO(fetchOperatorVO(driverRequest.getOperatorId()));
        driver.setAccountVO(userDetails.getAccountVO());
        if(action.equalsIgnoreCase("Save")){
            driver.setCreatedAt(LocalDateTime.now());
        }else{
            driver.setCreatedAt(driverRequest.getCreatedAt());
        }
        driver.setUpdatedAt(LocalDateTime.now());
        driver.setUpdatedBy(userDetails.getEmployeeId());

        return driver;
    }

    public DriverResponse voToDTOConverter(Driver driver){
        if (driver == null) {
            return new DriverResponse();
        }
        DriverResponse driverResponse = new DriverResponse();
        driverResponse.setDriverId(driver.getDriverId());
        driverResponse.setDriverName(driver.getDriverName());
        driverResponse.setDriverPhoneNo(driver.getDriverPhoneNo());
        driverResponse.setDriverLicenseNo(driver.getDriverLicenseNo());
        driverResponse.setCreatedAt(driver.getCreatedAt());
        driverResponse.setCreatedAt(driver.getCreatedAt());
        driverResponse.setUpdatedBy(driver.getUpdatedBy());

        return driverResponse;
    }

    @Transactional (propagation = Propagation.REQUIRED)
    public Driver internalFetchService(Integer driverId){
        if (driverId == null) {
            return new Driver();
        }
        Driver driver = driverRepo.findById(driverId).orElse(new Driver());
        return driver;
    }

    @Transactional(readOnly = true)
    public ResponseMessageDTO fetchAllDriver(Integer operatorId, int pageNo) {
        if (pageNo < 1) {
            pageNo = 1;
        }
        int elementCount = 25;
        Pageable pageable = PageRequest.of(pageNo-1,elementCount, Sort.by("driverId").ascending());
        Page<Driver> page = driverRepo.findByOperatorVO_OperatorId(operatorId,pageable);
        List<Driver> driverList = page.getContent();
        List<DriverResponse> driverResponseList = new ArrayList<>();
        for(Driver driver : driverList){
            driverResponseList.add(voToDTOConverter(driver));
        }
        ResponseMessageDTO responseMessageDTO = new ResponseMessageDTO();
        responseMessageDTO.setStatusCode(200);
        responseMessageDTO.setValue("DriverList", driverResponseList);
        responseMessageDTO.setValue("TotalPages",page.getTotalPages());
        responseMessageDTO.setValue("TotalElements",page.getTotalElements());
        return responseMessageDTO;
    }

    @Transactional (propagation = Propagation.REQUIRED)
    public ResponseEntity<String> saveDriver(DriverRequest driverRequest, String username, CustomizedUserDetails userDetails) {
        if (driverRequest == null || username == null || userDetails == null) {
            return ResponseEntity.status(400).body("Required metadata context missing");
        }

        Auth savedAuth = authService.saveUser(username);

        if(savedAuth != null){
            Driver savingDriver = dtoToVOConverter("Save", driverRequest,userDetails);
            savingDriver.setAccountVO(savedAuth);

            Operator getOperator = operatorService.internalFetchService(driverRequest.getOperatorId());
            if(getOperator != null && getOperator.getOperatorId() > 0){
                savingDriver.setOperatorVO(getOperator);
            }else{
                return ResponseEntity.status(400).body("Error: Operator ID " + driverRequest.getOperatorId() + " does not exist in the system.");
            }
            Driver savedDriver = driverRepo.save(savingDriver);

            if(savedDriver.getDriverId() != null && savedDriver.getDriverId() > 0){
                return ResponseEntity.status(200).body("Account created Successfully.");
            }else{
                return ResponseEntity.status(400).body("Invalid or Bad Request");
            }
        }

        return ResponseEntity.status(500).body("Internal server error");
    }

    @Transactional (propagation = Propagation.REQUIRED)
    public DriverResponse updateDriver(DriverRequest driverRequest, CustomizedUserDetails userDetails) {
        if (driverRequest == null) {
            return new DriverResponse();
        }
        Driver mutatedDriver = dtoToVOConverter("Update", driverRequest,userDetails);
        Driver updatedDriveVO = driverRepo.save(mutatedDriver);
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
    public DriverResponse findByDriverName(String driverName) {
        if (driverName == null || driverName.trim().isEmpty()) {
            return null;
        }

        Driver fetchDriver = driverRepo.findByDriverName(driverName);
        if(fetchDriver != null){
            return voToDTOConverter(fetchDriver);
        }
        return null;
    }
}
