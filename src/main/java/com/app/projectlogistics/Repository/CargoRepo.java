package com.app.projectlogistics.Repository;

import com.app.projectlogistics.ValueObject.CargoVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CargoRepo extends JpaRepository<CargoVO,Integer> {
}
