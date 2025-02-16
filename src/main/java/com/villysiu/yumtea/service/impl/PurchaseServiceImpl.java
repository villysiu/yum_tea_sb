package com.villysiu.yumtea.service.impl;

import com.villysiu.yumtea.models.cart.Cart;
import com.villysiu.yumtea.models.purchase.Purchase;
import com.villysiu.yumtea.models.purchase.PurchaseLineitem;
import com.villysiu.yumtea.models.user.User;
import com.villysiu.yumtea.dao.PurchaseProjection;


import com.villysiu.yumtea.repo.purchase.PurchaseLineitemRepo;
import com.villysiu.yumtea.repo.purchase.PurchaseRepo;
import com.villysiu.yumtea.service.CartService;
import com.villysiu.yumtea.service.PurchaseService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
public class PurchaseServiceImpl implements PurchaseService {

    private final CartService cartService;
    private final PurchaseRepo purchaseRepo;
    private final PurchaseLineitemRepo purchaseLineitemRepo;


//    public PurchaseServiceImpl(CustomUserDetailsServiceImpl userDetailsService, CartService cartService, PurchaseRepo purchaseRepo, PurchaseLineitemRepo purchaseLineitemRepo) {
//        this.userDetailsService = userDetailsService;
//        this.cartService = cartService;
//        this.purchaseRepo = purchaseRepo;
//        this.purchaseLineitemRepo = purchaseLineitemRepo;
//
//    }


    @Override
    public Long createPurchase(Map<String, Object> purchaseDto, User user) {

        List<Cart> carts = cartService.getCartsByUserId(user.getId());
        if(carts.isEmpty())
            throw new EntityNotFoundException("Cart is empty for user: " + user.getId());

        Purchase purchase = new Purchase();
        purchase.setUser(user);
        purchase.setPurchaseDate(new Date(System.currentTimeMillis()));
        purchase.setTip((Double) purchaseDto.get("tip"));
        purchase.setPurchaseLineitemList(new ArrayList<>());

        purchaseRepo.save(purchase);

        for(Cart cartLineitem : carts){
            System.out.println(cartLineitem.toString());
            PurchaseLineitem purchaseLineitem = new PurchaseLineitem();

            purchaseLineitem.setPurchase(purchase);
            purchaseLineitem.setMenuitem(cartLineitem.getMenuitem());
            purchaseLineitem.setMilk(cartLineitem.getMilk());
            purchaseLineitem.setSize(cartLineitem.getSize());
            purchaseLineitem.setSugar(cartLineitem.getSugar());
            purchaseLineitem.setTemperature(cartLineitem.getTemperature());

            purchaseLineitem.setQuantity(cartLineitem.getQuantity());
            purchaseLineitem.setPrice(cartLineitem.getPrice());

            purchaseLineitemRepo.save(purchaseLineitem);

            purchase.getPurchaseLineitemList().add(purchaseLineitem);
        }

        cartService.deleteCartsByUserId(user.getId());

        return purchase.getId();


    }



    @Override
    public List<PurchaseProjection> getPurchasesByUserId(Long userId){
        return purchaseRepo.findByUserId(userId, PurchaseProjection.class);

    }

    @Override
    public PurchaseProjection getPurchaseById(Long purchaseId) {
       return purchaseRepo.findById(purchaseId, PurchaseProjection.class )
               .orElseThrow(()->new EntityNotFoundException("Purchase id "+ purchaseId + " not found"));

    }
    @Override
    public void deletePurchaseById(Long purhcaseId){
        Purchase p = purchaseRepo.findById(purhcaseId, Purchase.class)
                .orElseThrow(()->new EntityNotFoundException("Purchase id "+ purhcaseId + " not found"));
        purchaseRepo.delete(p);
    }



}
