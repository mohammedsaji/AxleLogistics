package com.app.projectlogistics.Repository.UserDetailServicesRepo;

import com.app.projectlogistics.ValueObject.AccountVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthenticationRepo extends JpaRepository<AccountVO,Integer> {

}
