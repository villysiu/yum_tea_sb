package com.villysiu.yumtea.service.impl;

import com.villysiu.yumtea.dto.request.CartInputDto;
import com.villysiu.yumtea.models.cart.Cart;
import com.villysiu.yumtea.models.tea.*;
import com.villysiu.yumtea.models.user.Account;
import com.villysiu.yumtea.dto.response.CartProjection;
import com.villysiu.yumtea.models.user.Role;
import com.villysiu.yumtea.repo.cart.CartRepo;

import com.villysiu.yumtea.service.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {
    private final CartRepo cartRepo;
    private final MenuitemService menuitemService;
    private final MilkService milkService;
    private final RoleService roleService;
    private final SizeService sizeService;

    private static final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);
    @Autowired
    public CartServiceImpl(CartRepo cartRepo, MenuitemService menuitemService, MilkService milkService, RoleService roleService, SizeService sizeService) {
        this.cartRepo = cartRepo;
        this.menuitemService = menuitemService;
        this.milkService = milkService;
        this.roleService = roleService;
        this.sizeService = sizeService;

    }



    @Transactional
    @Override
    public Long createCart(CartInputDto cartInputDto, Account account) throws RuntimeException {
        System.out.println(cartInputDto);


        Optional<Cart> duplicatedCart = cartRepo.findByAccountIdAndMenuitemIdAndMilkIdAndSizeIdAndSugarAndTemperature(
                account.getId(),
                cartInputDto.getMenuitemId(),
                cartInputDto.getMilkId(),
                cartInputDto.getSizeId(),
                cartInputDto.getSugar(),
                cartInputDto.getTemperature()
        );

        try {
            //cart already existed, update quantity

            if (duplicatedCart.isPresent()) {
                logger.info("Duplicated cart found");
                Cart dupCart = duplicatedCart.get();
                logger.info("updating quantity");
                dupCart.setQuantity(dupCart.getQuantity() + cartInputDto.getQuantity());
                logger.info("updated quantity successfully");

                logger.info("saving cart");
                cartRepo.save(dupCart);
                logger.info("saved cart successfully");
                return dupCart.getId();
            } else {
                logger.info("Creating a new Cart");
                Cart newCart = new Cart();

                newCart.setAccount(account);

                Menuitem menuitem = menuitemService.getMenuitemById(cartInputDto.getMenuitemId());
                newCart.setMenuitem(menuitem);

                Milk milk = (menuitem.getMilk().getTitle().equals("NA")) ?
                        menuitem.getMilk() : milkService.getMilkById(cartInputDto.getMilkId());
                newCart.setMilk(milk);

                Size size = sizeService.getSizeById(cartInputDto.getSizeId());
                newCart.setSize(size);

                newCart.setPrice(menuitem.getPrice() + milk.getPrice() + size.getPrice());

                newCart.setQuantity(cartInputDto.getQuantity());

                newCart.setTemperature(
                        menuitem.getTemperature().equals(Temperature.FREE) ? cartInputDto.getTemperature() : menuitem.getTemperature()
                );
                newCart.setSugar(
                        menuitem.getSugar().equals(Sugar.NA) ? Sugar.NA : cartInputDto.getSugar()
                );
                logger.info("saving new Cart");
                cartRepo.save(newCart);
                logger.info("saved new Cart successfully");
                return newCart.getId();
            }
        } catch(RuntimeException e) {
            logger.info("Error saving new Cart");
            throw new RuntimeException("Error occurred while deleting purchases", e);
        }
    }

    @Transactional
    @Override
    public Long updateCart(Long id, CartInputDto cartInputDto, Account account) {

        Cart cart = cartRepo.findByIdAndAccountId(id, account.getId(), Cart.class)
                .orElseThrow(()->new NoSuchElementException("Cart not found"));

        Optional<Cart> duplicatedCart = cartRepo.findByAccountIdAndMenuitemIdAndMilkIdAndSizeIdAndSugarAndTemperature(
                account.getId(),
                cartInputDto.getMenuitemId(),
                cartInputDto.getMilkId(),
                cartInputDto.getSizeId(),
                cartInputDto.getSugar(),
                cartInputDto.getTemperature()
        );
        try {
            if (duplicatedCart.isPresent()) {
                logger.info("Duplicated cart found");
                logger.info("updating quantity in current cart");
                cart.setQuantity(cartInputDto.getQuantity() + duplicatedCart.get().getQuantity());
                logger.info("updating quantity successfully");

                logger.info("deleting duplicated cart");
                cartRepo.delete(duplicatedCart.get());
                logger.info("deleted duplicated cart successfully");

            }
            //        during update, only properties are allowed to update, not the menuitem
            //        Menuitem menuitem = menuitemService.getMenuitemById(cartInputDto.getMenuitemId());
            //        cart.setMenuitem(menuitem);
            else {
                Menuitem menuitem = cart.getMenuitem();

                Milk milk = (menuitem.getMilk().getTitle().equals("NA")) ?
                        menuitem.getMilk() : milkService.getMilkById(cartInputDto.getMilkId());
                cart.setMilk(milk);

                Size size = sizeService.getSizeById(cartInputDto.getSizeId());
                cart.setSize(size);

                cart.setPrice(menuitem.getPrice() + milk.getPrice() + size.getPrice());
                cart.setQuantity(cartInputDto.getQuantity());


                cart.setTemperature(
                        menuitem.getTemperature().equals(Temperature.FREE) ? cartInputDto.getTemperature() : menuitem.getTemperature()
                );
                cart.setSugar(
                        menuitem.getSugar().equals(Sugar.NA) ? Sugar.NA : cartInputDto.getSugar()
                );
            }
            logger.info("saving current cart");
            cartRepo.save(cart);
            logger.info("saved current cart successfully");

            return cart.getId();
        }catch(RuntimeException e) {
            logger.info("Error updating Cart");
            throw new RuntimeException("Error occurred while updating cart", e);
        }
    }


    @Override
    public List<CartProjection> getCartProjectionsByAccountId(Long accountId){
        return cartRepo.findByAccountIdOrderByIdDesc(accountId, CartProjection.class);
    }
    @Override
    public List<Cart> getCartsByAccountId(Long accountId){
        return cartRepo.findByAccountIdOrderByIdDesc(accountId, Cart.class);
    }


    @Override
    public CartProjection getCartProjectionById(Long id) {
        return cartRepo.findById(id, CartProjection.class).orElseThrow(()-> new NoSuchElementException("Cart not found"));
    }
    @Override
    public Cart getCartById(Long id){
        return cartRepo.findById(id, Cart.class).orElseThrow(()-> new NoSuchElementException("Cart not found"));
    }


    @Transactional
    @Override
    public void deleteCartsByAccountId(Long accountId, Account authenticatedAccount) {
        try{
            if(roleService.isAdmin(authenticatedAccount) || Objects.equals(authenticatedAccount.getId(), accountId)){
                logger.info("Deleting carts by {} ", accountId);
                cartRepo.deleteAllByAccountId(accountId);
                logger.info("Successfully deleted all cart by account {}", accountId);
            }
            else{
                logger.error("You do not have permission to delete purchases");
                throw new SecurityException("You do not have permission to delete purchases.");
            }
        } catch (Exception e){
            logger.error("Error occurred while deleting carts: " + e.getMessage(), e);
            throw new RuntimeException("Error occurred while deleting purchases", e);
        }

    }





    @Transactional
    @Override
    public void deleteCartById(Long id, Account authenticatedAccount) {
        Cart cart = cartRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found"));

        try {
            if(roleService.isAdmin(authenticatedAccount) || Objects.equals(authenticatedAccount.getId(), cart.getAccount().getId())){
            logger.info("Deleting cart {}", id);
            cartRepo.deleteById(id);
            logger.info("Successfully deleted cart {}", id);
            }
            else{
                logger.error("You do not have permission to delete cart");
                throw new SecurityException("You do not have permission to delete cart.");
        }
        } catch (Exception e){
            logger.error("Error occurred while deleting carts: " + e.getMessage(), e);
            throw new RuntimeException("Error occurred while deleting purchases", e);
        }
    }
    @PreAuthorize("hasRole('ROLE_ADMIN') or authentication.name==cart.account.email")
//  An error occurred: Failed to evaluate expression ' authentication.name==cart.account.email'
    public void deleteCart(Cart cart){
        logger.info("Deleting cart ");
        cartRepo.delete(cart);
        logger.info("Successfully deleted cart");
    }
}
