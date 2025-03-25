package com.villysiu.yumtea.service.purchase;

import com.villysiu.yumtea.dto.request.PurchaseRequest;
import com.villysiu.yumtea.models.cart.Cart;
import com.villysiu.yumtea.models.purchase.Purchase;
import com.villysiu.yumtea.models.purchase.PurchaseLineitem;
import com.villysiu.yumtea.models.user.Account;
import com.villysiu.yumtea.dto.response.PurchaseProjection;


import com.villysiu.yumtea.repo.purchase.PurchaseLineitemRepo;
import com.villysiu.yumtea.repo.purchase.PurchaseRepo;
import com.villysiu.yumtea.service.cart.CartService;

import com.villysiu.yumtea.service.user.RoleService;
import jakarta.persistence.EntityNotFoundException;
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

        logger.info("Getting carts by account {}", account.getEmail());
        List<Cart> carts = cartService.getCartsByAccountId(account.getId());
        if(carts.isEmpty()){
            logger.info("{} has empty cart", account.getEmail());
            throw new EntityNotFoundException("User's cart is empty.");
        }
        logger.info("Successfully getting carts by account");

        logger.info("creating a new purchase");
        Purchase purchase = new Purchase();
        purchase.setAccount(account);
        purchase.setPurchaseDate(new Date(System.currentTimeMillis()));

        double total = 0.0;

        purchase.setTax(purchaseRequest.getTax());
        purchase.setTip(purchaseRequest.getTip());

        purchase.setPurchaseLineitemList(new ArrayList<>());

        logger.info("saving purchase");
        purchaseRepo.save(purchase);
        logger.info("saved purchase");

        for(Cart cartLineitem : carts){
            logger.info("creating new purchaseLineitem for cart {}",cartLineitem.getId());
            PurchaseLineitem purchaseLineitem = new PurchaseLineitem();

            purchaseLineitem.setPurchase(purchase);
            purchaseLineitem.setMenuitem(cartLineitem.getMenuitem());
            purchaseLineitem.setMilk(cartLineitem.getMilk());
            purchaseLineitem.setSize(cartLineitem.getSize());
            purchaseLineitem.setSugar(cartLineitem.getSugar());
            purchaseLineitem.setTemperature(cartLineitem.getTemperature());

            purchaseLineitem.setQuantity(cartLineitem.getQuantity());
            purchaseLineitem.setPrice(cartLineitem.getPrice());

            logger.info("saving new purchaseLineitem for cart {}",cartLineitem.getId());
            purchaseLineitemRepo.save(purchaseLineitem);
            logger.info("successfully saved");
            purchase.getPurchaseLineitemList().add(purchaseLineitem);
            total += cartLineitem.getPrice() * cartLineitem.getQuantity();
        }


        purchase.setTotal(total + purchase.getTip() + purchase.getTax());

        logger.info("Saving purchase to database.");
        purchaseRepo.save(purchase);
        logger.info("Successfully saved purchase to database.");

        cartService.deleteCartsByAccountId(account.getId(), account);

        return purchase.getId();


    }



    @Override
    public List<PurchaseProjection> getPurchasesByAccountId(Long accountId){
        return purchaseRepo.findByAccountId(accountId, PurchaseProjection.class);
    }

    @Override
    public List<PurchaseProjection> getAllPurchases() {
        return purchaseRepo.findAllProjections();
    }

    @Override
    public PurchaseProjection getPurchaseById(Long purchaseId, Account account){

       return purchaseRepo.findByAccountIdAndPurchaseIdQuery(account.getId(), purchaseId, PurchaseProjection.class)
               .orElseThrow(()->new EntityNotFoundException("Purchase not found"));


    }
    @Transactional
    @Override
    public void deletePurchaseById(Long purchaseId, Account authenticatedAccount){
        try{
            if(roleService.isAdmin(authenticatedAccount)  || isOwner(purchaseId, authenticatedAccount.getId())){
                logger.info("Deleting purchase {}", purchaseId);
                purchaseRepo.deleteById(purchaseId);
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
            if(roleService.isAdmin(authenticatedAccount) || isOwnerOfAccount(accountId, authenticatedAccount.getId())){
                logger.info("Deleting purchases by {} ", accountId);
                purchaseRepo.deleteAllByAccountId(accountId);
                logger.info("Successfully deleted all purchases by account {}", accountId);
            }
            else{
                throw new SecurityException("You do not have permission to delete purchases.");
            }
        } catch (Exception e){
            throw new RuntimeException("Error occurred while deleting purchases", e);
        }

    }


    private boolean isOwner(Long purchaseId, Long authenticatedId){
         return purchaseRepo.existsByIdAndAccountId(purchaseId, authenticatedId);
    }
    private boolean isOwnerOfAccount(Long acountId, Long authenticatedId){
        return authenticatedId.equals(acountId);
    }

}
