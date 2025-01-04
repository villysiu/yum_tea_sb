package com.villysiu.yumtea.repo.cart;

import com.villysiu.yumtea.models.cart.Cart;
import com.villysiu.yumtea.models.tea.Sugar;
import com.villysiu.yumtea.models.tea.Temperature;
import com.villysiu.yumtea.projection.CartProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CartRepo extends JpaRepository<Cart, Long> {
    //    List<CartLineitem> findByUserId(Long id);
    List<CartProjection> findByUserId(Long userId);

    Cart findByUserIdAndMenuitemIdAndMilkIdAndSizeIdAndSugarAndTemperature(

            Long user_id,
            Long menuitem_id,
            Long milk_id,
            Long size_id,
            Sugar sugar,
            Temperature temperature
    );
}
