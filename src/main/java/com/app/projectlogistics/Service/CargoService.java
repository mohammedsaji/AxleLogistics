package com.app.projectlogistics.Service;

import com.app.projectlogistics.DataTransferObject.Cargo.RQTCargoDTO;
import com.app.projectlogistics.DataTransferObject.Cargo.RSPCargoDTO;
import com.app.projectlogistics.Repository.CargoRepo;
import com.app.projectlogistics.ValueObject.CargoVO;
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
    public CargoVO saveCargo(RQTCargoDTO rqtCargoDTO){
        if (rqtCargoDTO == null) {
            throw new IllegalArgumentException("Cargo request data payload cannot be null");
        }
        CargoVO savingCargoVO = dtoToVOConverter(rqtCargoDTO);
        CargoVO savedCargoVO = cargoRepo.save(savingCargoVO);
        return savedCargoVO;
    }

    public CargoVO dtoToVOConverter(RQTCargoDTO rqtCargoDTO){
        if (rqtCargoDTO == null) {
            return new CargoVO();
        }
        CargoVO cargoVO = new CargoVO();
        cargoVO.setCargoId(rqtCargoDTO.getCargoId());
        cargoVO.setCargoName(rqtCargoDTO.getCargoName());
        cargoVO.setCargoQuantity(rqtCargoDTO.getCargoQuantity());
        cargoVO.setCargoWeight(rqtCargoDTO.getCargoWeight());
        cargoVO.setCargoType(rqtCargoDTO.getCargoType());
        cargoVO.setCargoDescription(rqtCargoDTO.getCargoDescription());
        cargoVO.setCreatedAt(rqtCargoDTO.getCreatedAt());
        cargoVO.setUpdatedAt(rqtCargoDTO.getUpdatedAt());
        cargoVO.setUpdatedBy(rqtCargoDTO.getUpdatedBy());

        return cargoVO;
    }

    public RSPCargoDTO voToDtoConverter(CargoVO cargoVO){
        if (cargoVO == null) {
            return new RSPCargoDTO();
        }
        RSPCargoDTO rspCargoDTO = new RSPCargoDTO();
        rspCargoDTO.setCargoId(cargoVO.getCargoId());
        rspCargoDTO.setCargoName(cargoVO.getCargoName());
        rspCargoDTO.setCargoType(cargoVO.getCargoType());
        rspCargoDTO.setCargoDescription(cargoVO.getCargoDescription());
        rspCargoDTO.setCargoQuantity(cargoVO.getCargoQuantity());
        rspCargoDTO.setCargoWeight(cargoVO.getCargoWeight());
        rspCargoDTO.setCreatedAt(cargoVO.getCreatedAt());
        rspCargoDTO.setUpdatedAt(cargoVO.getUpdatedAt());
        rspCargoDTO.setUpdatedBy(cargoVO.getUpdatedBy());

        return rspCargoDTO;
    }

    @Transactional(readOnly = true)
    public RSPCargoDTO fetchCargo(Integer cargoId){
        if (cargoId == null) {
            return new RSPCargoDTO();
        }
        CargoVO fetchCargoVO = cargoRepo.findById(cargoId).orElse(new CargoVO());
        return voToDtoConverter(fetchCargoVO);
    }

    @Transactional (propagation = Propagation.REQUIRED)
    public CargoVO internalFetchService(Integer cargoId){
        if (cargoId == null) {
            return new CargoVO();
        }
        return cargoRepo.findById(cargoId).orElse(new CargoVO());
    }

}
