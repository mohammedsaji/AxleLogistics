package com.app.projectlogistics.Repository;

import com.app.projectlogistics.ValueObject.CustomerVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepo extends JpaRepository<CustomerVO, Integer> {

    public CustomerVO findByCustomerName(String customerName);
}
