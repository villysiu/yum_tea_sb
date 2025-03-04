package com.villysiu.yumtea.service;


import com.villysiu.yumtea.dto.request.PurchaseRequest;
import com.villysiu.yumtea.dto.response.PurchaseProjection;
import com.villysiu.yumtea.models.user.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Map;

public interface PurchaseService {
    // return id for the newly create purchase order
    Long createPurchase(PurchaseRequest purchaseRequest, User user);

    //return a list of all purchases by the current user
    List<PurchaseProjection> getPurchasesByUserId(Long userId);


    PurchaseProjection getPurchaseById(Long purchaseId, User user);

//    Purchase getPurchaseById(Long id);
    void deletePurchaseById(Long purchaseId, Long userId);
}
