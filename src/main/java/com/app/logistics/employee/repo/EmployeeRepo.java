package com.app.logistics.employee.repo;

import com.app.logistics.employee.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee,Integer> {

    Employee findByEmployeeName(String employeeName);
}
