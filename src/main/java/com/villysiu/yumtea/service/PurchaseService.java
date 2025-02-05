package com.villysiu.yumtea.service;


import com.villysiu.yumtea.models.purchase.Purchase;
import com.villysiu.yumtea.dao.PurchaseProjection;
import com.villysiu.yumtea.models.user.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PurchaseService {
    // return id for the newly create purchase order
    Long createPurchase(Map<String, Object> purchaseDto, User user);

    //return a list of all purchases by the current user
    List<PurchaseProjection> getPurchasesByUserId(Long userId);


    PurchaseProjection getPurchaseById(Long id);
}
