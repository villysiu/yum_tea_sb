package com.villysiu.yumtea.service.impl;

import com.villysiu.yumtea.dto.input.CartInputDto;
import com.villysiu.yumtea.dto.output.CartOutputDto;
import com.villysiu.yumtea.models.cart.Cart;
import com.villysiu.yumtea.models.tea.Menuitem;
import com.villysiu.yumtea.models.tea.Milk;
import com.villysiu.yumtea.models.tea.Size;
import com.villysiu.yumtea.models.user.User;
import com.villysiu.yumtea.repo.cart.CartRepo;


import com.villysiu.yumtea.service.*;
import com.villysiu.yumtea.validation.EmailExistsException;
import lombok.RequiredArgsConstructor;
import org.hibernate.boot.beanvalidation.IntegrationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepo cartRepo;
    private final MenuitemService menuitemService;
    private final MilkService milkService;

    private final SizeService sizeService;
    private final UserService userService;

    @Override
    public CartOutputDto createCart(CartInputDto cartInputDto) throws RuntimeException {

        User user = userService.getCurrentUser();

        Cart cart = cartRepo.findByUserIdAndMenuitemIdAndMilkIdAndSizeIdAndSugarAndTemperature(
                user.getId(),
                cartInputDto.getMenuitemId(),
                cartInputDto.getMilkId(),
                cartInputDto.getSizeId(),
                cartInputDto.getSugar(),
                cartInputDto.getTemperature()
        );
        //cart already existed, update quantity
        if(cart != null){
            cart.setQuantity(cart.getQuantity() + cartInputDto.getQuantity());
        }
        else{
            cart = new Cart();
            cart.setUser(user);
            Menuitem menuitem = menuitemService.getMenuitemById(cartInputDto.getMenuitemId());
            cart.setMenuitem(menuitem);

            Milk milk = milkService.getMilkById(cartInputDto.getMilkId());
            cart.setMilk(milk);

            Size size = sizeService.getSizeById(cartInputDto.getSizeId());
            cart.setSize(size);

            cart.setPrice(cartInputDto.getPrice());
            cart.setQuantity(cartInputDto.getQuantity());
            cart.setTemperature(cartInputDto.getTemperature());
            cart.setSugar(cartInputDto.getSugar());
            System.out.println(cart);

        }
        // still need to catch??
        //java.sql.SQLIntegrityConstraintViolationException: Duplicate entry '2-6-3-1-HOT-FIFTY' for key 'cart.UniqueCartAndUser'

//        try{
            cartRepo.save(cart);
//        } catch(DataIntegrityViolationException e){
//            throw new RuntimeException("Data integrity violation occurred", e);
//        }

        return helper(cart);
    }

    @Override
    public CartOutputDto updateCart(Long id, CartInputDto cartInputDto) throws RuntimeException {
        User user = userService.getCurrentUser();
        Cart cart = cartRepo.findById(id).orElseThrow(()->new RuntimeException("Cart not found."));

        Cart duplicatedCart = cartRepo.findByUserIdAndMenuitemIdAndMilkIdAndSizeIdAndSugarAndTemperature(
                user.getId(),
                cartInputDto.getMenuitemId(),
                cartInputDto.getMilkId(),
                cartInputDto.getSizeId(),
                cartInputDto.getSugar(),
                cartInputDto.getTemperature()
        );
        if(duplicatedCart != null){
            duplicatedCart.setQuantity(cartInputDto.getQuantity() + duplicatedCart.getQuantity());
            cartRepo.save(duplicatedCart);
            cartRepo.delete(cart);
            return helper(duplicatedCart);

        }

        Menuitem menuitem = menuitemService.getMenuitemById(cartInputDto.getMenuitemId());
        cart.setMenuitem(menuitem);

        Milk milk = milkService.getMilkById(cartInputDto.getMilkId());
        cart.setMilk(milk);

        Size size = sizeService.getSizeById(cartInputDto.getSizeId());
        cart.setSize(size);

        cart.setPrice(cartInputDto.getPrice());
        cart.setQuantity(cartInputDto.getQuantity());
        cart.setTemperature(cartInputDto.getTemperature());
        cart.setSugar(cartInputDto.getSugar());

        cartRepo.save(cart);

        return helper(cart);
    }

    private CartOutputDto helper(Cart data ){
        CartOutputDto dest = new CartOutputDto();
        dest.setId(data.getId());
        dest.setMenuitemId(data.getMenuitem().getId());
        dest.setMenuitemTitle(data.getMenuitem().getTitle());

        dest.setMilkId(data.getMilk().getId());
        dest.setMilkTitle(data.getMilk().getTitle());

        dest.setSizeId(data.getSize().getId());
        dest.setSizeTitle(data.getSize().getTitle());

        dest.setQuantity(data.getQuantity());
        dest.setPrice(data.getPrice());
        dest.setSugar(data.getSugar());
        dest.setTemperature(data.getTemperature());

        return dest;
    }
}
