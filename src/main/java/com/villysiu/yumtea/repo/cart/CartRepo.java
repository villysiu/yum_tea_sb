package com.villysiu.yumtea.repo.cart;

import com.villysiu.yumtea.models.cart.Cart;
import com.villysiu.yumtea.models.tea.Sugar;
import com.villysiu.yumtea.models.tea.Temperature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepo extends JpaRepository<Cart, Long> {


    Optional<Cart> findByUserIdAndMenuitemIdAndMilkIdAndSizeIdAndSugarAndTemperature(

            Long user_id,
            Long menuitem_id,
            Long milk_id,
            Long size_id,
            Sugar sugar,
            Temperature temperature
    );
//    Optional<Cart> findById(Long id) throws NoSuchElementException;
    // returning either cartProjection or Cart
    <T> List<T> findByUserId(Long id, Class<T> type);


    <T> Optional<T> findById(Long id, Class<T> type);
//    OR
//    <T> T findById(Long id, Class<T> type);
}
