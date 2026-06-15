package com.app.projectlogistics.Repository;

import com.app.projectlogistics.ValueObject.DriverVO;
import com.app.projectlogistics.ValueObject.VehicleVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepo extends JpaRepository<VehicleVO,Integer> {

    public Page<VehicleVO> findByOperatorVO_OperatorId(Integer operatorId, Pageable pageable);

    public VehicleVO findByVehicleNumber(String vehicleNumber);
}
