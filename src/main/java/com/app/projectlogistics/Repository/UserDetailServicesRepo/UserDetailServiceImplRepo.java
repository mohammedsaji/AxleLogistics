package com.app.projectlogistics.Repository.UserDetailServicesRepo;

import com.app.projectlogistics.ValueObject.AccountVO;
import com.app.projectlogistics.ValueObject.EmployeeVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDetailServiceImplRepo extends JpaRepository<AccountVO,Integer> {

    public AccountVO findByAccountUsername(String username);
}
