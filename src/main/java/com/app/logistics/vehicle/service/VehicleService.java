package com.app.logistics.vehicle.service;

import com.app.logistics.auth.authUtils.AuthDetails;
import com.app.logistics.common.exception.APIException;
import com.app.logistics.operator.entity.Operator;
import com.app.logistics.operator.service.OperatorService;
import com.app.logistics.vehicle.dto.VehicleRequest;
import com.app.logistics.vehicle.dto.VehicleResponse;
import com.app.logistics.vehicle.entity.Vehicle;
import com.app.logistics.vehicle.repo.VehicleRepo;
import com.app.logistics.vehicle.utils.VehicleMapper;
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
public class VehicleService {

    private final VehicleRepo vehicleRepo;
    private final VehicleMapper vehicleMapper;
    private final OperatorService operatorService;

    public VehicleService(VehicleRepo vehicleRepo,
                          VehicleMapper vehicleMapper,
                          OperatorService operatorService) {
        this.vehicleRepo = vehicleRepo;
        this.vehicleMapper = vehicleMapper;
        this.operatorService = operatorService;
    }

    @Transactional(readOnly = true)
    public VehicleResponse fetchVehicle(Integer vehicleId) {
        if (vehicleId == null) {
            throw new APIException("Vehicle ID cannot be null", HttpStatus.BAD_REQUEST);
        }
        Vehicle vehicle = vehicleRepo.findById(vehicleId)
                .orElseThrow(() -> new APIException("Vehicle not found for ID: " + vehicleId, HttpStatus.NOT_FOUND));
        return toResponseWithOperator(vehicle);
    }

    private VehicleResponse toResponseWithOperator(Vehicle vehicle) {
        VehicleResponse response = vehicleMapper.toDTO(vehicle);
        if (vehicle.getOperatorVO() != null) {
            response.setOperatorId(vehicle.getOperatorVO().getOperatorId());
        }
        return response;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Vehicle internalFetchService(Integer vehicleId) {
        if (vehicleId == null) {
            return new Vehicle();
        }
        return vehicleRepo.findById(vehicleId).orElse(new Vehicle());
    }

    @Transactional(readOnly = true)
    public List<VehicleResponse> fetchAllVehicle(Integer operatorId, int pageNo) {
        if (pageNo < 1) {
            pageNo = 1;
        }
        int elementCount = 25;
        Pageable pageable = PageRequest.of(pageNo - 1, elementCount, Sort.by("vehicleId").ascending());
        Page<Vehicle> page = vehicleRepo.findByOperatorVO_OperatorId(operatorId, pageable);

        return page.getContent().stream()
                .map(this::toResponseWithOperator)
                .collect(Collectors.toList());
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public VehicleResponse saveVehicle(VehicleRequest vehicleRequest, AuthDetails authDetails) {
        if (vehicleRequest == null || authDetails == null) {
            throw new APIException("Payload request and user context are required to register a vehicle", HttpStatus.BAD_REQUEST);
        }

        Operator operator = operatorService.internalFetchService(vehicleRequest.getOperatorId());
        if (operator == null) {
            throw new APIException("Operator ID " + vehicleRequest.getOperatorId() + " does not exist in the system.", HttpStatus.BAD_REQUEST);
        }

        Vehicle savingVehicle = vehicleMapper.toVO(vehicleRequest);
        savingVehicle.setOperatorVO(operator);
        savingVehicle.setUpdatedBy(authDetails.getEmployeeId());

        Vehicle savedVehicle = vehicleRepo.save(savingVehicle);
        return toResponseWithOperator(savedVehicle);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public VehicleResponse updateVehicle(VehicleRequest vehicleRequest, AuthDetails authDetails) {
        if (vehicleRequest == null) {
            throw new APIException("Vehicle request data payload cannot be null", HttpStatus.BAD_REQUEST);
        }

        Operator operator = operatorService.internalFetchService(vehicleRequest.getOperatorId());
        if (operator == null) {
            throw new APIException("Operator ID " + vehicleRequest.getOperatorId() + " does not exist in the system.", HttpStatus.BAD_REQUEST);
        }

        Vehicle mutatedVehicle = vehicleMapper.toVO(vehicleRequest);
        mutatedVehicle.setOperatorVO(operator);
        if (authDetails != null) {
            mutatedVehicle.setUpdatedBy(authDetails.getEmployeeId());
        }

        Vehicle updatedVehicle = vehicleRepo.save(mutatedVehicle);
        return toResponseWithOperator(updatedVehicle);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteVehicle(Integer vehicleId) {
        if (vehicleId == null) {
            throw new APIException("Vehicle ID parameter cannot be null", HttpStatus.BAD_REQUEST);
        }
        if (!vehicleRepo.existsById(vehicleId)) {
            throw new APIException("Vehicle not found for ID: " + vehicleId, HttpStatus.NOT_FOUND);
        }
        vehicleRepo.deleteById(vehicleId);
    }

    @Transactional(readOnly = true)
    public VehicleResponse fetchByVehicleNumber(String vehicleNumber) {
        if (vehicleNumber == null || vehicleNumber.trim().isEmpty()) {
            throw new APIException("Vehicle number cannot be empty", HttpStatus.BAD_REQUEST);
        }
        Vehicle vehicle = vehicleRepo.findByVehicleNumber(vehicleNumber);
        if (vehicle == null) {
            throw new APIException("Vehicle not found for number: " + vehicleNumber, HttpStatus.NOT_FOUND);
        }
        return toResponseWithOperator(vehicle);
    }
}