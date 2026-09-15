package com.app.logistics.employee.repo;

import com.app.logistics.employee.entity.Employee;
import com.app.logistics.manager.entity.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee,Integer> {

    Employee findByEmployeeName(String employeeName);

    Optional<Employee> findByAccount_AccountId(Integer accountId);
}
