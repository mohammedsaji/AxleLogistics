package com.app.projectlogistics.Repository;

import com.app.projectlogistics.ValueObject.DriverVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Driver;

@Repository
public interface DriverRepo extends JpaRepository<DriverVO,Integer> {

    public Page<DriverVO> findByOperatorVO_OperatorId(Integer operatorId, Pageable pageable);

    public DriverVO findByDriverName(String driverName);
}
