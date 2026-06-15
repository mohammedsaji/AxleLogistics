package com.app.projectlogistics.Repository;

import com.app.projectlogistics.ValueObject.ShipmentVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShipmentRepo extends JpaRepository<ShipmentVO,Integer> {

    public ShipmentVO findByShipmentStatusVO_ShippingStatusId(Integer shipmentStatusId);

}
