package com.villysiu.yumtea.service.impl;

import com.villysiu.yumtea.models.cart.Cart;
import com.villysiu.yumtea.models.purchase.Purchase;
import com.villysiu.yumtea.models.purchase.PurchaseLineitem;
import com.villysiu.yumtea.models.user.User;
import com.villysiu.yumtea.projection.CartProjection;
import com.villysiu.yumtea.projection.PurchaseProjection;
import com.villysiu.yumtea.repo.cart.CartRepo;


import com.villysiu.yumtea.repo.purchase.PurchaseLineitemRepo;
import com.villysiu.yumtea.repo.purchase.PurchaseRepo;
import com.villysiu.yumtea.service.CartService;
import com.villysiu.yumtea.service.PurchaseService;
import com.villysiu.yumtea.service.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PurchaseServiceImpl implements PurchaseService {
    private final UserService userService;
    private final CartService cartService;

    private final PurchaseRepo purchaseRepo;
    private final PurchaseLineitemRepo purchaseLineitemRepo;
//    private final CartRepo cartRepo;

    @Override
    public Long createPurchase() {

        User user = userService.getCurrentUser();
        List<Cart> cart = cartService.getCartsByUserId(user.getId());
        Purchase purchase = new Purchase();
        purchase.setUser(user);
        purchase.setPurchaseDate(new Date(System.currentTimeMillis()));
        purchase.setPurchaseLineitemList(new ArrayList<PurchaseLineitem>());
        purchaseRepo.save(purchase);
        for(Cart cartLineitem : cart){
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
//            System.out.println(purchaseLineitem.toString());
            purchase.getPurchaseLineitemList().add(purchaseLineitem);
        }

//        cartService.removeUserCart(cart);

        return purchase.getId();

    }


    @Override
    public List<PurchaseProjection> getUserPurchases(Long userId) {
        System.out.println(" hhhhdkjfkdf");
        return purchaseRepo.findByUserId(userId, PurchaseProjection.class);
//        return purchaseRepo.findAllProjectionByUserId(userId);
    }
    @Override
    public List<Purchase> getFullPurchases(Long userId){
        return purchaseRepo.findByUserId(userId, Purchase.class);
    }

//    @Override
//    public List<PurchaseProjection> getPurchaseProjectionWithChildren(Long userId){
//        return purchaseRepo.findAllWithPurchaseLineitems();
//    }
    @Override
    public PurchaseProjection getPurchaseById(Long purchaseId) {

        try {
            return purchaseRepo.findById(purchaseId, PurchaseProjection.class);
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Cart not found");
        }
//        return purchaseRepo.findById(purchaseId).orElseThrow(()->new RuntimeException("not foung"));
    }



}
