package com.app.logistics.cargo.service;

import com.app.logistics.cargo.dto.CargoRequest;
import com.app.logistics.cargo.dto.CargoResponse;
import com.app.logistics.cargo.repo.CargoRepo;
import com.app.logistics.cargo.entity.Cargo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CargoService {

    private CargoRepo cargoRepo;

    public CargoService(CargoRepo cargoRepo){
        this.cargoRepo = cargoRepo;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Cargo saveCargo(CargoRequest cargoRequest){
        if (cargoRequest == null) {
            throw new IllegalArgumentException("Cargo request data payload cannot be null");
        }
        Cargo savingCargo = dtoToVOConverter(cargoRequest);
        Cargo savedCargo = cargoRepo.save(savingCargo);
        return savedCargo;
    }

    public Cargo dtoToVOConverter(CargoRequest cargoRequest){
        if (cargoRequest == null) {
            return new Cargo();
        }
        Cargo cargo = new Cargo();
        cargo.setCargoId(cargoRequest.getCargoId());
        cargo.setCargoName(cargoRequest.getCargoName());
        cargo.setCargoQuantity(cargoRequest.getCargoQuantity());
        cargo.setCargoWeight(cargoRequest.getCargoWeight());
        cargo.setCargoType(cargoRequest.getCargoType());
        cargo.setCargoDescription(cargoRequest.getCargoDescription());
        cargo.setCreatedAt(cargoRequest.getCreatedAt());
        cargo.setUpdatedAt(cargoRequest.getUpdatedAt());
        cargo.setUpdatedBy(cargoRequest.getUpdatedBy());

        return cargo;
    }

    public CargoResponse voToDtoConverter(Cargo cargo){
        if (cargo == null) {
            return new CargoResponse();
        }
        CargoResponse cargoResponse = new CargoResponse();
        cargoResponse.setCargoId(cargo.getCargoId());
        cargoResponse.setCargoName(cargo.getCargoName());
        cargoResponse.setCargoType(cargo.getCargoType());
        cargoResponse.setCargoDescription(cargo.getCargoDescription());
        cargoResponse.setCargoQuantity(cargo.getCargoQuantity());
        cargoResponse.setCargoWeight(cargo.getCargoWeight());
        cargoResponse.setCreatedAt(cargo.getCreatedAt());
        cargoResponse.setUpdatedAt(cargo.getUpdatedAt());
        cargoResponse.setUpdatedBy(cargo.getUpdatedBy());

        return cargoResponse;
    }

    @Transactional(readOnly = true)
    public CargoResponse fetchCargo(Integer cargoId){
        if (cargoId == null) {
            return new CargoResponse();
        }
        Cargo fetchCargo = cargoRepo.findById(cargoId).orElse(new Cargo());
        return voToDtoConverter(fetchCargo);
    }

    @Transactional (propagation = Propagation.REQUIRED)
    public Cargo internalFetchService(Integer cargoId){
        if (cargoId == null) {
            return new Cargo();
        }
        return cargoRepo.findById(cargoId).orElse(new Cargo());
    }

}
