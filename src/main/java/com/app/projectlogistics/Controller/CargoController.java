package com.app.projectlogistics.Controller;

import com.app.projectlogistics.DataTransferObject.Cargo.RSPCargoDTO;
import com.app.projectlogistics.Service.CargoService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("logistic/cargo")
public class CargoController {

    private final CargoService cargoService;

    public CargoController(CargoService cargoService){
        this.cargoService = cargoService;
    }

    @GetMapping("/fetch")
    @ResponseBody
    public ResponseEntity<RSPCargoDTO> fetchCargo(@RequestParam Integer cargoId){
        RSPCargoDTO result = cargoService.fetchCargo(cargoId);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.noContent().build();
    }
}