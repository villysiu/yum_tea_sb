package com.villysiu.yumtea.repo.purchase;


import com.villysiu.yumtea.models.purchase.Purchase;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PurchaseRepo extends JpaRepository<Purchase, Long> {

    <T>Optional<T> findById(Long id, Class<T> type);
    <T> List<T> findByUserId(Long userId, Class<T> type);
//    List<Purchase> findByUserId(Long userId);
    List<Purchase> findAll();

//    @Query("SELECT u FROM User u WHERE u.email = :email")
//    User findByEmail(@Param("email") String email);

}
