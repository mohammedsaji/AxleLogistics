package com.app.logistics.operator.repo;

import com.app.logistics.operator.entity.Operator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperatorRepo extends JpaRepository<Operator,Integer> {

    public Page<Operator> findByOperatorTransportType(String operatorTransportType, Pageable pageable);

    public Operator findByOperatorName(String operatorName);
}
