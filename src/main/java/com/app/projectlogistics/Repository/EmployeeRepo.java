package com.app.projectlogistics.Repository;

import com.app.projectlogistics.ValueObject.EmployeeVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepo extends JpaRepository<EmployeeVO,Integer> {

    EmployeeVO findByEmployeeName(String employeeName);
}
