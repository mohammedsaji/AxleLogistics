package com.app.logistics.shipmentStatus.repo;

import com.app.logistics.shipmentStatus.entity.ShipmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusRepo extends JpaRepository<ShipmentStatus,Integer> {
}
