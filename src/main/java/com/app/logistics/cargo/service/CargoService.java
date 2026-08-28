package com.app.logistics.cargo.service;

import com.app.logistics.cargo.dto.CargoRequest;
import com.app.logistics.cargo.dto.CargoResponse;
import com.app.logistics.cargo.repo.CargoRepo;
import com.app.logistics.cargo.entity.Cargo;
import com.app.logistics.cargo.utils.CargoMapper;
import com.app.logistics.common.exception.APIException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CargoService {

    private final CargoRepo cargoRepo;
    private final CargoMapper cargoMapper;

    public CargoService(CargoRepo cargoRepo, CargoMapper cargoMapper) {
        this.cargoRepo = cargoRepo;
        this.cargoMapper = cargoMapper;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Cargo saveCargo(CargoRequest cargoRequest) {
        if (cargoRequest == null) {
            throw new APIException("Cargo request data payload cannot be null", HttpStatus.BAD_REQUEST);
        }
        Cargo savingCargo = cargoMapper.toVO(cargoRequest);
        return cargoRepo.save(savingCargo);
    }

    @Transactional(readOnly = true)
    public CargoResponse fetchCargo(Integer cargoId) {
        if (cargoId == null) {
            throw new APIException("Cargo ID cannot be null", HttpStatus.BAD_REQUEST);
        }
        Cargo cargo = cargoRepo.findById(cargoId)
                .orElseThrow(() -> new APIException("Cargo not found for ID: " + cargoId, HttpStatus.NOT_FOUND));
        return cargoMapper.toDTO(cargo);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Cargo internalFetchService(Integer cargoId) {
        if (cargoId == null) {
            throw new APIException("Cargo ID cannot be null", HttpStatus.BAD_REQUEST);
        }
        return cargoRepo.findById(cargoId)
                .orElseThrow(() -> new APIException("Cargo not found for ID: " + cargoId, HttpStatus.NOT_FOUND));
    }
}