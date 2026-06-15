package com.app.dummyPack.repository;

import com.app.dummyPack.vo.AuthVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRepo extends JpaRepository<AuthVO,Integer> {
}
