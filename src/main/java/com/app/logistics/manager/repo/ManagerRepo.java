package com.app.logistics.manager.repo;

import com.app.logistics.manager.entity.Manager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ManagerRepo extends JpaRepository<Manager,Integer> {

    Page<Manager> findByOperatorVO_OperatorId(Integer operatorId, Pageable pageable);

    List<Manager> findByOperatorVO_OperatorId(Integer operatorId);

    Manager findByManagerName(String managerName);
}
