package com.villysiu.yumtea.service;


import com.villysiu.yumtea.dto.response.PurchaseProjection;
import com.villysiu.yumtea.models.user.User;

import java.util.List;
import java.util.Map;

public interface PurchaseService {
    // return id for the newly create purchase order
    Long createPurchase(Map<String, Object> purchaseDto, User user);

    //return a list of all purchases by the current user
    List<PurchaseProjection> getPurchasesByUserId(Long userId);


    PurchaseProjection getPurchaseById(Long id);

//    Purchase getPurchaseById(Long id);
    void deletePurchaseById(Long id);
}
