package com.villysiu.yumtea.repo.purchase;


import com.villysiu.yumtea.models.purchase.Purchase;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


public interface PurchaseRepo extends JpaRepository<Purchase, Long> {

    <T> List<T> findByUserId(Long userId, Class<T> type);

    @Query("SELECT p FROM Purchase p WHERE p.id = :purchaseId AND p.user.id = :userId")
    <T> Optional<T> findByUserIdAndPurchaseIdQuery(@Param("userId") Long userId, @Param("purchaseId") Long purchaseId,
                                               Class<T> type);



}
