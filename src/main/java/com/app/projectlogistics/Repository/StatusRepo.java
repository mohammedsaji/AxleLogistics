package com.app.projectlogistics.Repository;

import com.app.projectlogistics.ValueObject.ShipmentStatusVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusRepo extends JpaRepository<ShipmentStatusVO,Integer> {
}
