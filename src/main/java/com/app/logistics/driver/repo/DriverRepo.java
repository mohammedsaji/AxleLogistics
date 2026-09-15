package com.app.logistics.driver.repo;

import com.app.logistics.driver.entity.Driver;
import com.app.logistics.manager.entity.Manager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DriverRepo extends JpaRepository<Driver,Integer> {

    public Page<Driver> findByOperator_OperatorId(Integer operatorId, Pageable pageable);

    public Driver findByDriverName(String driverName);

    Optional<Driver> findByAccount_AccountId(Integer accountId);
}
