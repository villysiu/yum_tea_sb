package com.villysiu.yumtea.service;

import com.villysiu.yumtea.dto.request.CartInputDto;

import com.villysiu.yumtea.models.cart.Cart;
import com.villysiu.yumtea.dao.CartProjection;
import com.villysiu.yumtea.models.user.User;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface CartService {

    // ALL
    List<Object[]> getCartsByUserQuery(User user);
    List<CartProjection> getCartProjectionsByUserId(Long userId);
    List<Cart> getCartsByUserId(Long userId);

    //SINGLE
    // used by update or remove
    Cart getCartById(Long id);

    // used for return to frontend
    CartProjection getCartProjectionById(Long id);

    // CUD
    Long createCart(CartInputDto cartInputDto, User user);
    Long updateCart(Long id, CartInputDto cartInputDto, User user);

    void deleteCartById(Long id);
    void deleteCartsByUserId(Long userId);
    //admin only
    void deleteCarts();




}
