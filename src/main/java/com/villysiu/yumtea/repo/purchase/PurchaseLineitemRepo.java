package com.villysiu.yumtea.repo.purchase;

import com.villysiu.yumtea.models.purchase.PurchaseLineitem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseLineitemRepo extends JpaRepository<PurchaseLineitem, Long> {
}
