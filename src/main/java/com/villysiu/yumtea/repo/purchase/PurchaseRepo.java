package com.villysiu.yumtea.repo.purchase;


import com.villysiu.yumtea.dto.response.PurchaseProjection;
import com.villysiu.yumtea.models.purchase.Purchase;

import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PurchaseRepo extends JpaRepository<Purchase, Long> {

    <T> List<T> findByAccountId(Long accountId, Class<T> type);

    @Query("SELECT p FROM Purchase p ORDER BY p.purchaseDate ASC")
    List<PurchaseProjection> findAllProjections();



    //Looks up purchase by Account id and only return if account owns tha purchase, otherwaise null
    @Query("SELECT p FROM Purchase p WHERE p.id = :purchaseId AND p.account.id = :accountId")
    <T> Optional<T> findByAccountIdAndPurchaseIdQuery(
            @Param("accountId") Long accountId, @Param("purchaseId") Long purchaseId, Class<T> type);

    Boolean existsByIdAndAccountId( Long id, Long accountId);

    void deleteByAccountId(Long accountId);
    void deleteAllByAccountId(Long accountId);
}
