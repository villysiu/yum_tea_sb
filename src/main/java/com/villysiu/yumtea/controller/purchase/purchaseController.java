package com.villysiu.yumtea.controller.purchase;

import com.villysiu.yumtea.models.purchase.Purchase;
import com.villysiu.yumtea.models.user.User;

import com.villysiu.yumtea.dto.response.PurchaseProjection;
import com.villysiu.yumtea.service.PurchaseService;
import com.villysiu.yumtea.service.impl.CustomUserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;



@RestController
public class purchaseController {
    @Autowired
    private final PurchaseService purchaseService;
    @Autowired
    private final CustomUserDetailsServiceImpl userDetailsService;

    public purchaseController(PurchaseService purchaseService, CustomUserDetailsServiceImpl userDetailsService) {
        this.purchaseService = purchaseService;
        this.userDetailsService = userDetailsService;
    }


    /*
    Admin can access all the purchases
    User can see only his purchases

     */
    @GetMapping("/purchases")
    public List<PurchaseProjection> getPurchases(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userDetailsService.findByEmail(userDetails.getUsername());
         return purchaseService.getPurchasesByUserId(user.getId());
    }

    @GetMapping("/purchases/{id}")
    public PurchaseProjection getPurchases(@PathVariable("id") Long id, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userDetailsService.findByEmail(userDetails.getUsername());
        PurchaseProjection p = purchaseService.getPurchaseById(id);
        System.out.println(p.getPurchaseDate());
        return purchaseService.getPurchaseById(id);
    }

    @PostMapping("/purchase")
    public ResponseEntity<PurchaseProjection> createPurchase(@RequestBody Map<String, Object> purchaseDto, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userDetailsService.findByEmail(userDetails.getUsername());
//        {"tip": 10.55}
        Long id = purchaseService.createPurchase(purchaseDto, user);
        return new ResponseEntity<>(purchaseService.getPurchaseById(id), HttpStatus.CREATED);
    }

    @DeleteMapping("/purchases/{id}")
    public ResponseEntity<Void> deletePurchase(@PathVariable("id") Long id){
        purchaseService.deletePurchaseById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
