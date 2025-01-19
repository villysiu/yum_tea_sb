package com.villysiu.yumtea.repo.purchase;

import com.villysiu.yumtea.models.purchase.Purchase;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseRepo extends JpaRepository<Purchase, Long> {

    <T> List<T> findByUserId(Long userId, Class<T> type);

    <T> T findById(Long purchaseId, Class<T> type);
//    <T> List<T> findAll(Class<T> type); //Cannot be overridden

//List<PurchaseProjection> findAllProjectionByUserId(Long userId);

}
