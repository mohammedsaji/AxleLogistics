package com.app.logistics.shipment.repo;

import com.app.logistics.shipment.entity.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShipmentRepo extends JpaRepository<Shipment,Integer> {

    public Shipment findByShipmentStatusVO_ShippingStatusId(Integer shipmentStatusId);

}
