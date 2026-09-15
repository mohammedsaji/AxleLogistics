package com.app.logistics.manager.repo;

import com.app.logistics.manager.entity.Manager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ManagerRepo extends JpaRepository<Manager, Integer> {

    Page<Manager> findByOperator_OperatorId(Integer operatorId, Pageable pageable);

    List<Manager> findByOperator_OperatorId(Integer operatorId);

    Manager findByManagerName(String managerName);

    @Query("""
            SELECT m FROM Manager m
            WHERE m.operator.operatorId = :operatorId
            AND m.managerStatus = 'ACTIVE'
            """)
    Optional<Manager> findByActiveManager(@Param("operatorId") Integer operatorId);

    @Query("""
                SELECT m FROM Manager m 
                WHERE m.operator.operatorId = :operatorId 
                  AND m.managerId <> :deactivatedManagerId 
                  AND m.managerStatus = 'IN_ACTIVE' 
                ORDER BY m.managerId ASC
            """)
    Optional<Manager> findAlternateManagerToActivate(@Param("operatorId") Integer operatorId,@Param("deactivatedManagerId") Integer deactivatedManagerId);

    Optional<Manager> findByAccount_AccountId(Integer accountId);
}
