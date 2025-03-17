package com.villysiu.yumtea.service.impl;

import com.villysiu.yumtea.dto.request.PurchaseRequest;
import com.villysiu.yumtea.models.cart.Cart;
import com.villysiu.yumtea.models.purchase.Purchase;
import com.villysiu.yumtea.models.purchase.PurchaseLineitem;
import com.villysiu.yumtea.models.user.Account;
import com.villysiu.yumtea.dto.response.PurchaseProjection;


import com.villysiu.yumtea.repo.purchase.PurchaseLineitemRepo;
import com.villysiu.yumtea.repo.purchase.PurchaseRepo;
import com.villysiu.yumtea.service.AuthorizationService;
import com.villysiu.yumtea.service.CartService;
import com.villysiu.yumtea.service.PurchaseService;

import com.villysiu.yumtea.service.TaxRateService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PurchaseServiceImpl implements PurchaseService {

    private final CartService cartService;
    private final PurchaseRepo purchaseRepo;
    private final PurchaseLineitemRepo purchaseLineitemRepo;
    private final TaxRateService taxRateService;
    private final RoleService roleService;
    @Autowired
    public PurchaseServiceImpl(CartService cartService, PurchaseRepo purchaseRepo, PurchaseLineitemRepo purchaseLineitemRepo, TaxRateService taxRateService,  RoleService roleService) {
        this.cartService = cartService;
        this.purchaseRepo = purchaseRepo;
        this.purchaseLineitemRepo = purchaseLineitemRepo;
        this.taxRateService = taxRateService;
        this.roleService = roleService;
    }

    private static final Logger logger = LoggerFactory.getLogger(PurchaseServiceImpl.class);

    @Transactional
    @Override
    public Long createPurchase(PurchaseRequest purchaseRequest, Account account) {

        List<Cart> carts = cartService.getCartsByAccountId(account.getId());
        if(carts.isEmpty())
            throw new RuntimeException("User's cart is empty.");

        Purchase purchase = new Purchase();
        purchase.setAccount(account);
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

        cartService.deleteCartsByAccountId(account.getId(), account);

        return purchase.getId();


    }



    @Override
    public List<PurchaseProjection> getPurchasesByAccountId(Long accountId){
        return purchaseRepo.findByAccountId(accountId, PurchaseProjection.class);
    }

    @Override
    public PurchaseProjection getPurchaseById(Long purchaseId, Account account){

       return purchaseRepo.findByAccountIdAndPurchaseIdQuery(account.getId(), purchaseId, PurchaseProjection.class)
               .orElseThrow(()->new NoSuchElementException("Purchase not found"));


    }
    @Transactional
    @Override
    public void deletePurchaseById(Long purchaseId, Account authenticatedAccount){
        try{
            Purchase purchase = purchaseRepo.findById(purchaseId)
                    .orElseThrow(()->new NoSuchElementException("Purchase not found"));
            if(roleService.isAdmin(authenticatedAccount)  || Objects.equals(authenticatedAccount.getId(), purchase.getAccount().getId())){
                logger.info("Deleting purchase {}", purchaseId);
                purchaseRepo.delete(purchase);
                logger.info("Successfully deleted purchase {}", purchaseId);
            }
            else{
                throw new SecurityException("You do not have permission to delete purchases.");
            }
        } catch (Exception e){
            throw new RuntimeException("Error occurred while deleting purchases", e);
        }


    }

//    purchase owner and admin can delete all purchases
    @Transactional
    @Override
    public void deletePurchasesByAccountId(Long accountId, Account authenticatedAccount) throws SecurityException{
        try{
            if(roleService.isAdmin(authenticatedAccount) || Objects.equals(authenticatedAccount.getId(), accountId)){
                purchaseRepo.deleteAllByAccountId(accountId);
            }
            else{
                throw new SecurityException("You do not have permission to delete purchases.");
            }
        } catch (Exception e){
            throw new RuntimeException("Error occurred while deleting purchases", e);
        }

    }

}
