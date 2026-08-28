package com.app.logistics.driver.service;

import com.app.logistics.auth.entity.Auth;
import com.app.logistics.auth.authUtils.AuthDetails;
import com.app.logistics.auth.service.AuthService;
import com.app.logistics.common.exception.APIException;
import com.app.logistics.driver.dto.DriverRequest;
import com.app.logistics.driver.dto.DriverResponse;
import com.app.logistics.driver.repo.DriverRepo;
import com.app.logistics.driver.entity.Driver;
import com.app.logistics.driver.utils.DriverMapper;
import com.app.logistics.operator.entity.Operator;
import com.app.logistics.operator.service.OperatorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DriverService {

    private final DriverRepo driverRepo;
    private final DriverMapper driverMapper;
    private final OperatorService operatorService;
    private final AuthService authService;

    public DriverService(DriverRepo driverRepo,
                         DriverMapper driverMapper,
                         OperatorService operatorService,
                         AuthService authService) {
        this.driverRepo = driverRepo;
        this.driverMapper = driverMapper;
        this.operatorService = operatorService;
        this.authService = authService;
    }

    @Transactional(readOnly = true)
    public DriverResponse fetchDriver(Integer driverId) {
        if (driverId == null) {
            throw new APIException("Driver ID cannot be null", HttpStatus.BAD_REQUEST);
        }
        Driver driver = driverRepo.findById(driverId)
                .orElseThrow(() -> new APIException("Driver not found for ID: " + driverId, HttpStatus.NOT_FOUND));
        return driverMapper.toDTO(driver);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Driver internalFetchService(Integer driverId) {
        if (driverId == null) {
            throw new APIException("Driver ID cannot be null", HttpStatus.BAD_REQUEST);
        }
        return driverRepo.findById(driverId)
                .orElseThrow(() -> new APIException("Driver not found for ID: " + driverId, HttpStatus.NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public List<DriverResponse> fetchAllDriver(Integer operatorId, int pageNo) {
        if (pageNo < 1) {
            pageNo = 1;
        }
        int elementCount = 25;
        Pageable pageable = PageRequest.of(pageNo - 1, elementCount, Sort.by("driverId").ascending());
        Page<Driver> page = driverRepo.findByOperatorVO_OperatorId(operatorId, pageable);

        return page.getContent().stream()
                .map(driverMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public DriverResponse saveDriver(DriverRequest driverRequest, String username, AuthDetails authDetails) {
        if (driverRequest == null || username == null || authDetails == null) {
            throw new APIException("Required metadata context missing", HttpStatus.BAD_REQUEST);
        }

        Auth linkedAuth = authService.saveUser(username);

        Operator operator = operatorService.internalFetchService(driverRequest.getOperatorId());
        if (operator == null) {
            throw new APIException("Operator ID " + driverRequest.getOperatorId() + " does not exist in the system.", HttpStatus.BAD_REQUEST);
        }

        Driver savingDriver = driverMapper.toVO(driverRequest);
        savingDriver.setAccountVO(linkedAuth);
        savingDriver.setOperatorVO(operator);
        savingDriver.setUpdatedBy(authDetails.getEmployeeId());

        Driver savedDriver = driverRepo.save(savingDriver);
        return driverMapper.toDTO(savedDriver);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public DriverResponse updateDriver(DriverRequest driverRequest, AuthDetails authDetails) {
        if (driverRequest == null) {
            throw new APIException("Driver request data payload cannot be null", HttpStatus.BAD_REQUEST);
        }

        Operator operator = operatorService.internalFetchService(driverRequest.getOperatorId());
        if (operator == null) {
            throw new APIException("Operator ID " + driverRequest.getOperatorId() + " does not exist in the system.", HttpStatus.BAD_REQUEST);
        }

        Driver mutatedDriver = driverMapper.toVO(driverRequest);
        mutatedDriver.setOperatorVO(operator);
        if (authDetails != null) {
            mutatedDriver.setUpdatedBy(authDetails.getEmployeeId());
        }

        Driver updatedDriver = driverRepo.save(mutatedDriver);
        return driverMapper.toDTO(updatedDriver);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteDriver(Integer driverId) {
        if (driverId == null) {
            throw new APIException("Driver identifier parameter cannot be null", HttpStatus.BAD_REQUEST);
        }
        if (!driverRepo.existsById(driverId)) {
            throw new APIException("Driver not found for ID: " + driverId, HttpStatus.NOT_FOUND);
        }
        driverRepo.deleteById(driverId);
    }

    @Transactional(readOnly = true)
    public DriverResponse findByDriverName(String driverName) {
        if (driverName == null || driverName.trim().isEmpty()) {
            throw new APIException("Driver name cannot be empty", HttpStatus.BAD_REQUEST);
        }
        Driver driver = driverRepo.findByDriverName(driverName);
        if (driver == null) {
            throw new APIException("Driver not found for name: " + driverName, HttpStatus.NOT_FOUND);
        }
        return driverMapper.toDTO(driver);
    }
}