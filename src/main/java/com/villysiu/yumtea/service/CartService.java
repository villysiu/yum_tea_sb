package com.villysiu.yumtea.service;

import com.villysiu.yumtea.dto.request.CartInputDto;

import com.villysiu.yumtea.models.cart.Cart;
import com.villysiu.yumtea.dto.response.CartProjection;
import com.villysiu.yumtea.models.user.Account;

import java.util.List;

public interface CartService {

    // ALL
    List<CartProjection> getCartProjectionsByAccountId(Long accountId);
    List<Cart> getCartsByAccountId(Long accountId);


    // used for return to frontend
    CartProjection getCartProjectionById(Long id);

    // CUD
    Long createCart(CartInputDto cartInputDto, Account account);
    Long updateCart(Long id, CartInputDto cartInputDto, Account account);


    void deleteCartById(Long id, Account authenticatedAccount);
    void deleteCartsByAccountId(Long accountId, Account authenticatedAccount) ;






}
