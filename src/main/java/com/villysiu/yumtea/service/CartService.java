package com.villysiu.yumtea.service;

import com.villysiu.yumtea.dto.request.CartInputDto;

import com.villysiu.yumtea.models.cart.Cart;
import com.villysiu.yumtea.dto.response.CartProjection;
import com.villysiu.yumtea.models.user.Account;

import java.util.List;

public interface CartService {

    // ALL
    List<CartProjection> getCartProjectionsByUserId(Long accountId);
    List<Cart> getCartsByUserId(Long accountId);

    //SINGLE
    // used by update or remove
    Cart getCartById(Long id);

    // used for return to frontend
    CartProjection getCartProjectionById(Long id);

    // CUD
    Long createCart(CartInputDto cartInputDto, Account account);
    Long updateCart(Long id, CartInputDto cartInputDto, Account account);

    void deleteCartById(Long id);
    void deleteCartsByUserId(Long accountId);
    //admin only
    void deleteCarts();




}
