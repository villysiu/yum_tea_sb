package com.villysiu.yumtea.service;

import com.villysiu.yumtea.dto.tea.CartLineitemDto;
import com.villysiu.yumtea.models.CartLineitem;

public interface CartLineitemService {
    CartLineitem createCartLineitem(CartLineitemDto cartLineitemDto) throws RuntimeException;
//    Cart updateCartLineitem(CartDto cartDto) throws RuntimeException;
}
