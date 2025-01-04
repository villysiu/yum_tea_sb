package com.villysiu.yumtea.repo.cart;

import com.villysiu.yumtea.models.cart.Cart;
import com.villysiu.yumtea.projection.CartProjection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepo extends JpaRepository<Cart, Long> {
    //    List<CartLineitem> findByUserId(Long id);
    List<CartProjection> findByUserId(Long userId);
}
