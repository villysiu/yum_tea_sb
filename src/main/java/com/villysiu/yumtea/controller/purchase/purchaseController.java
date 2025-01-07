package com.villysiu.yumtea.controller.purchase;

import com.villysiu.yumtea.models.purchase.Purchase;
import com.villysiu.yumtea.models.user.User;

import com.villysiu.yumtea.projection.PurchaseProjection;
import com.villysiu.yumtea.repo.purchase.PurchaseRepo;
import com.villysiu.yumtea.service.PurchaseService;
import com.villysiu.yumtea.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class purchaseController {
    @Autowired
    private final PurchaseService purchaseService;
    private final UserService userService;
    private final PurchaseRepo purchaseRepo;
    /*
    Admin can access all the purchases
    User can see only his purchases

     */
    @GetMapping("/purchases")
    public List<PurchaseProjection> getUserPurchase(){
         User user = userService.getCurrentUser();
//         if(user.getRole().name().equals("ADMIN") ) {
//             return purchaseService.getPurchases();
//             return null;
//         }

         return purchaseService.getUserPurchases(user.getId());
    }

    @GetMapping("/fullpurchases")
    public List<Purchase> getFullPurchase(){
        User user = userService.getCurrentUser();
        return purchaseService.getFullPurchases(user.getId());
    }
//    @GetMapping("/purchaseProjectionWithChildren")
//    public List<PurchaseProjection> getPurchaseProjectionWithChildren(){
//        User user = userService.getCurrentUser();
//        return purchaseService.getPurchaseProjectionWithChildren(user.getId());
//    }

    @PostMapping("/purchase")
    public ResponseEntity<PurchaseProjection> createPurchase() {

        Long id = purchaseService.createPurchase();
        return new ResponseEntity<>(purchaseService.getPurchaseById(id), HttpStatus.CREATED);
    }

}
