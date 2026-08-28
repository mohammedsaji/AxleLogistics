package com.app.logistics.vehicle.repo;

import com.app.logistics.vehicle.entity.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepo extends JpaRepository<Vehicle,Integer> {

    public Page<Vehicle> findByOperatorVO_OperatorId(Integer operatorId, Pageable pageable);

    public Vehicle findByVehicleNumber(String vehicleNumber);
}
