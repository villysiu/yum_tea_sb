package com.villysiu.yumtea.service.impl;

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
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
public class PurchaseServiceImpl implements PurchaseService {

    private final CartService cartService;
    private final PurchaseRepo purchaseRepo;
    private final PurchaseLineitemRepo purchaseLineitemRepo;
    private final TaxRateService taxRateService;


    @Override
    public Long createPurchase(Map<String, Object> purchaseDto, User user) {

        List<Cart> carts = cartService.getCartsByUserId(user.getId());
        if(carts.isEmpty())
            throw new EntityNotFoundException("Cart is empty for user: " + user.getId());

        Purchase purchase = new Purchase();
        purchase.setUser(user);
        purchase.setPurchaseDate(new Date(System.currentTimeMillis()));
//        purchase.setTip((Double) purchaseDto.get("tip"));
        Object tip = purchaseDto.get("tip");

        if (tip instanceof Double) {
            purchase.setTip((Double) tip);
        } else if (tip instanceof Number) {
            purchase.setTip(((Number) tip).doubleValue());
        } else {
            purchase.setTip(0.0);
        }
        purchase.setPurchaseLineitemList(new ArrayList<>());

        purchaseRepo.save(purchase);

        double total = 0.0;
        double taxRate = taxRateService.getTaxRateByState(purchaseDto.get("state").toString());


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
