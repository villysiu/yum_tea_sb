package com.villysiu.yumtea.service;

import com.villysiu.yumtea.dto.input.CartInputDto;
import com.villysiu.yumtea.dto.output.CartOutputDto;

public interface CartService {
    CartOutputDto createCart(CartInputDto cartInputDto) throws RuntimeException;

    CartOutputDto updateCart(Long id, CartInputDto cartInputDto) throws RuntimeException;
}
