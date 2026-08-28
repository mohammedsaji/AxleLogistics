package com.app.logistics.driver.repo;

import com.app.logistics.driver.entity.Driver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverRepo extends JpaRepository<Driver,Integer> {

    public Page<Driver> findByOperatorVO_OperatorId(Integer operatorId, Pageable pageable);

    public Driver findByDriverName(String driverName);
}
