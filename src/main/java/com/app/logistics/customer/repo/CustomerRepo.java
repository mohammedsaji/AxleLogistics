package com.app.logistics.customer.repo;

import com.app.logistics.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepo extends JpaRepository<Customer, Integer> {

    public Customer findByCustomerName(String customerName);
}
