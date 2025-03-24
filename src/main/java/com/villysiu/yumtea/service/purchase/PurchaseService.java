package com.villysiu.yumtea.service.purchase;


import com.villysiu.yumtea.dto.request.PurchaseRequest;
import com.villysiu.yumtea.dto.response.PurchaseProjection;
import com.villysiu.yumtea.models.user.Account;

import java.util.List;

public interface PurchaseService {
    // return id for the newly create purchase order
    Long createPurchase(PurchaseRequest purchaseRequest, Account account);

    //return a list of all purchases by the current user
    List<PurchaseProjection> getPurchasesByAccountId(Long accountId);
    List<PurchaseProjection> getAllPurchases();

    PurchaseProjection getPurchaseById(Long purchaseId, Account account);

//    Purchase getPurchaseById(Long id);
    void deletePurchaseById(Long purchaseId, Account authenticatedAccount);
    void deletePurchasesByAccountId(Long accountId, Account authenticatedAccount);
}
