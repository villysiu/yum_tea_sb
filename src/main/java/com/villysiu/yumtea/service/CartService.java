package com.villysiu.yumtea.service;

import com.villysiu.yumtea.dto.input.CartInputDto;
import com.villysiu.yumtea.dto.output.CartOutputDto;

public interface CartService {
    Long createCart(CartInputDto cartInputDto) throws RuntimeException;

    Long updateCart(Long id, CartInputDto cartInputDto) throws RuntimeException;
}
