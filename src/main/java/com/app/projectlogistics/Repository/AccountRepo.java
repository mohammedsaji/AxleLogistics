package com.app.projectlogistics.Repository;

import com.app.projectlogistics.ValueObject.AccountVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepo extends JpaRepository<AccountVO,Integer> {
}
