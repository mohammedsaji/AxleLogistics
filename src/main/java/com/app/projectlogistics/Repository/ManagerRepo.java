package com.app.projectlogistics.Repository;

import com.app.projectlogistics.ValueObject.ManagerVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ManagerRepo extends JpaRepository<ManagerVO,Integer> {

    Page<ManagerVO> findByOperatorVO_OperatorId(Integer operatorId, Pageable pageable);

    List<ManagerVO> findByOperatorVO_OperatorId(Integer operatorId);

    ManagerVO findByManagerName(String managerName);
}
