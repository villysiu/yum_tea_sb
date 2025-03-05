package com.villysiu.yumtea.repo.purchase;

import com.villysiu.yumtea.models.purchase.TaxRate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface TaxRepo extends JpaRepository <TaxRate, Long>{
    Optional<TaxRate> findByState(String state);

}
