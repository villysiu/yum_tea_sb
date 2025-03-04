package com.villysiu.yumtea.service.impl;

import com.villysiu.yumtea.dto.request.PurchaseRequest;
import com.villysiu.yumtea.exception.EntityNotBelongToUserException;
import com.villysiu.yumtea.models.cart.Cart;
import com.villysiu.yumtea.models.purchase.Purchase;
import com.villysiu.yumtea.models.purchase.PurchaseLineitem;
import com.villysiu.yumtea.models.user.User;
import com.villysiu.yumtea.dto.response.PurchaseProjection;


import com.villysiu.yumtea.repo.purchase.PurchaseLineitemRepo;
import com.villysiu.yumtea.repo.purchase.PurchaseRepo;
import com.villysiu.yumtea.service.CartService;
import com.villysiu.yumtea.service.PurchaseService;

import com.villysiu.yumtea.service.TaxRateService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PurchaseServiceImpl implements PurchaseService {
    @Autowired
    private final CartService cartService;
    @Autowired
    private final PurchaseRepo purchaseRepo;
    @Autowired
    private final PurchaseLineitemRepo purchaseLineitemRepo;
    @Autowired
    private final TaxRateService taxRateService;

    public PurchaseServiceImpl(CartService cartService, PurchaseRepo purchaseRepo, PurchaseLineitemRepo purchaseLineitemRepo, TaxRateService taxRateService, CustomUserDetailsServiceImpl userDetailsService) {
        this.cartService = cartService;
        this.purchaseRepo = purchaseRepo;
        this.purchaseLineitemRepo = purchaseLineitemRepo;
        this.taxRateService = taxRateService;
    }

    @Override
    public Long createPurchase(PurchaseRequest purchaseRequest, User user) {

        List<Cart> carts = cartService.getCartsByUserId(user.getId());
        if(carts.isEmpty())
            throw new RuntimeException("User's cart is empty.");

        Purchase purchase = new Purchase();
        purchase.setUser(user);
        purchase.setPurchaseDate(new Date(System.currentTimeMillis()));

        double total = 0.0;
        Double taxRate = taxRateService.getTaxRateByState(purchaseRequest.getState());
        Double tip = purchaseRequest.getTip();
        purchase.setTip(tip == null ? 0 : tip);

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
            total += cartLineitem.getPrice() * cartLineitem.getQuantity();
        }

        purchase.setTax(total * taxRate / 100 );
        purchase.setTotal(total + purchase.getTip() + purchase.getTax());
        purchaseRepo.save(purchase);

        cartService.deleteCartsByUserId(user.getId());

        return purchase.getId();


    }



    @Override
    public List<PurchaseProjection> getPurchasesByUserId(Long userId){
        return purchaseRepo.findByUserId(userId, PurchaseProjection.class);
    }

    @Override
    public PurchaseProjection getPurchaseById(Long purchaseId, User user){

       return purchaseRepo.findByUserIdAndPurchaseIdQuery(user.getId(), purchaseId, PurchaseProjection.class)
               .orElseThrow(()->new NoSuchElementException("Purchase not found"));


    }
    @Override
    public void deletePurchaseById(Long purchaseId, Long userId){
        Purchase purchase = purchaseRepo.findByUserIdAndPurchaseIdQuery(userId, purchaseId, Purchase.class)
                .orElseThrow(()->new NoSuchElementException("Purchase not found"));

        purchaseRepo.delete(purchase);
    }

}
