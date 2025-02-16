package com.villysiu.yumtea.repo.purchase;

import com.villysiu.yumtea.models.purchase.Purchase;
import com.villysiu.yumtea.models.purchase.TaxRate;
import com.villysiu.yumtea.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaxRepo extends JpaRepository <TaxRate, Long>{
    Optional<TaxRate> findByState(String state);

}
