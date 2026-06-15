package com.app.dummyPack.repository;

import com.app.dummyPack.vo.BkVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BkRepo extends JpaRepository<BkVO,Integer> {
}
