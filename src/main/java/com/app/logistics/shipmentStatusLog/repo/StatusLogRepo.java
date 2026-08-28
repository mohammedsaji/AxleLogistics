package com.app.logistics.shipmentStatusLog.repo;

import com.app.logistics.shipmentStatusLog.entity.ShipmentStatusLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusLogRepo extends JpaRepository<ShipmentStatusLog,Integer> {
}
