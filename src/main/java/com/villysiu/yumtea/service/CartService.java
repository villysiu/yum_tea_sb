package com.villysiu.yumtea.service;

import com.villysiu.yumtea.dto.request.CartInputDto;

import com.villysiu.yumtea.models.cart.Cart;
import com.villysiu.yumtea.dao.CartProjection;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CartService {
    Long createCart(CartInputDto cartInputDto) throws RuntimeException;

    Long updateCart(Long id, CartInputDto cartInputDto) throws RuntimeException;

    List<Cart> getCartsByUserId(Long id);
    List<CartProjection> getCartProjectionsByUserId(Long id);

    Cart getCartById(Long id);
    CartProjection getCartProjectionById(Long id);

    ResponseEntity<String> removeUserCart(List<Cart> userCarts);
}
