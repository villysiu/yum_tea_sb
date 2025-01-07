package com.villysiu.yumtea.service.impl;

import com.villysiu.yumtea.dto.input.CartInputDto;
import com.villysiu.yumtea.models.cart.Cart;
import com.villysiu.yumtea.models.tea.Menuitem;
import com.villysiu.yumtea.models.tea.Milk;
import com.villysiu.yumtea.models.tea.Size;
import com.villysiu.yumtea.models.user.User;
import com.villysiu.yumtea.projection.CartProjection;
import com.villysiu.yumtea.repo.cart.CartRepo;


import com.villysiu.yumtea.service.*;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepo cartRepo;
    private final MenuitemService menuitemService;
    private final MilkService milkService;

    private final SizeService sizeService;
    private final UserService userService;

    @Override
    public Long createCart(CartInputDto cartInputDto) throws RuntimeException {

        User user = userService.getCurrentUser();

        Optional<Cart> cart = cartRepo.findByUserIdAndMenuitemIdAndMilkIdAndSizeIdAndSugarAndTemperature(
                user.getId(),
                cartInputDto.getMenuitemId(),
                cartInputDto.getMilkId(),
                cartInputDto.getSizeId(),
                cartInputDto.getSugar(),
                cartInputDto.getTemperature()
        );
        //cart already existed, update quantity
        if(cart.isPresent()){
            Cart dupCart = cart.get();
            dupCart.setQuantity(dupCart.getQuantity() + cartInputDto.getQuantity());
//
            cartRepo.save(dupCart);
            return dupCart.getId();
        }
        else{
            Cart newCart = new Cart();
            newCart.setUser(user);

            Menuitem menuitem = menuitemService.getMenuitemById(cartInputDto.getMenuitemId());
            newCart.setMenuitem(menuitem);

            Milk milk = milkService.getMilkById(cartInputDto.getMilkId());
            newCart.setMilk(milk);

            Size size = sizeService.getSizeById(cartInputDto.getSizeId());
            newCart.setSize(size);

            newCart.setPrice(cartInputDto.getPrice());
            newCart.setQuantity(cartInputDto.getQuantity());
            newCart.setTemperature(cartInputDto.getTemperature());
            newCart.setSugar(cartInputDto.getSugar());
            System.out.println(newCart);

            cartRepo.save(newCart);
            return newCart.getId();


        }
        // still need to catch??
        //java.sql.SQLIntegrityConstraintViolationException: Duplicate entry '2-6-3-1-HOT-FIFTY' for key 'cart.UniqueCartAndUser'

//
//        return helper(cart);
    }

    @Override
    public Long updateCart(Long id, CartInputDto cartInputDto) throws RuntimeException {
        User user = userService.getCurrentUser();
        Cart cart = cartRepo.findById(id).orElseThrow(()-> new NoSuchElementException("Cart not found"));

        Optional<Cart> duplicatedCart = cartRepo.findByUserIdAndMenuitemIdAndMilkIdAndSizeIdAndSugarAndTemperature(
                user.getId(),
                cartInputDto.getMenuitemId(),
                cartInputDto.getMilkId(),
                cartInputDto.getSizeId(),
                cartInputDto.getSugar(),
                cartInputDto.getTemperature()
        );
        if(duplicatedCart.isPresent()){
            duplicatedCart.get().setQuantity(cartInputDto.getQuantity() + duplicatedCart.get().getQuantity());
            cartRepo.save(duplicatedCart.get());
            cartRepo.delete(cart);
            return duplicatedCart.get().getId();

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

        return cart.getId();
    }

    @Override
    public List<Cart> getCartsByUserId(Long id) {

        return cartRepo.findByUserId(id, Cart.class);
    }
    @Override
    public List<CartProjection> getCartProjectionsByUserId(Long id){
        return cartRepo.findByUserId(id, CartProjection.class);
    }
    @Override
    public Cart getCartById(Long id) throws NoSuchElementException {
        try{
            return cartRepo.findById(id, Cart.class);
        }catch (NoSuchElementException e){
            throw new NoSuchElementException("Cart not found");
        }

    }
    @Override
    public CartProjection getCartProjectionById(Long id) throws NoSuchElementException {
        try {
            return cartRepo.findById(id, CartProjection.class);
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Cart not found");
        }
    }

    @Override
    public ResponseEntity<String> removeUserCart(List<Cart> userCarts){
//        cartRepo.deleteById(id);
        cartRepo.deleteAll(userCarts);
        return ResponseEntity.ok("Cart removed");
    }

}
