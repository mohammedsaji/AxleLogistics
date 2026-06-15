package com.app.projectlogistics.Repository;

import com.app.projectlogistics.ValueObject.OperatorVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperatorRepo extends JpaRepository<OperatorVO,Integer> {

    public Page<OperatorVO> findByOperatorTransportType(String operatorTransportType, Pageable pageable);

    public OperatorVO findByOperatorName(String operatorName);
}
