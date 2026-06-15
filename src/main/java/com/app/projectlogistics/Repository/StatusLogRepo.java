package com.app.projectlogistics.Repository;

import com.app.projectlogistics.ValueObject.ShipmentStatusLogVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusLogRepo extends JpaRepository<ShipmentStatusLogVO,Integer> {
}
