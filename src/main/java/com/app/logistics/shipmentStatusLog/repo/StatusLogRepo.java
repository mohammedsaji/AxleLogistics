package com.app.logistics.shipmentStatusLog.repo;

import com.app.logistics.shipmentStatusLog.entity.ShipmentStatusLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StatusLogRepo extends JpaRepository<ShipmentStatusLog, Integer> {
    Optional<ShipmentStatusLog> findFirstByShipment_ShippingIdOrderByUpdatedAtDesc(Integer shippingId);

    /**
     * Shipping IDs where the LATEST log row (by UPDATED_AT, per shipment)
     * belongs to the given driver account and isn't DELIVERED yet.
     * "Latest per shipment" avoids matching a shipment the driver was
     * on earlier but has since been reassigned away from.
     */
    @Query("""
            SELECT s.shipment.shippingId FROM ShipmentStatusLog s
            WHERE s.driver.account.accountId = :driverAccountId
              AND (s.shippingStatus IS NULL OR s.shippingStatus != 'DELIVERED')
              AND s.updatedAt = (
                  SELECT MAX(s2.updatedAt) FROM ShipmentStatusLog s2
                  WHERE s2.shipment.shippingId = s.shipment.shippingId
              )
            """)
    List<Integer> findActiveShippingIdsByDriverAccountId(@Param("driverAccountId") Integer driverAccountId);
}

