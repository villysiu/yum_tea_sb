package com.villysiu.yumtea.controller.purchase;

import com.villysiu.yumtea.dto.request.PurchaseRequest;
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


@RestController
public class PurchaseController {
    @Autowired
    private final PurchaseService purchaseService;
    @Autowired
    private final CustomUserDetailsServiceImpl userDetailsService;

    public PurchaseController(PurchaseService purchaseService, CustomUserDetailsServiceImpl userDetailsService) {
        this.purchaseService = purchaseService;
        this.userDetailsService = userDetailsService;
    }


    @GetMapping("/purchases")
    public List<PurchaseProjection> getPurchases(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userDetailsService.findByEmail(userDetails.getUsername());
         return purchaseService.getPurchasesByUserId(user.getId());
    }

    @GetMapping("/purchases/{id}")
    public PurchaseProjection getPurchaseById(@PathVariable("id") Long id,
                                           @AuthenticationPrincipal UserDetails userDetails) {
        User user = userDetailsService.findByEmail(userDetails.getUsername());

        return purchaseService.getPurchaseById(id, user);
    }

    @PostMapping("/purchase")
    public ResponseEntity<PurchaseProjection> createPurchase(@RequestBody PurchaseRequest purchaseRequest, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userDetailsService.findByEmail(userDetails.getUsername());
//        {"tip": 10.55, stata: "WA"}
        Long id = purchaseService.createPurchase(purchaseRequest, user);
        return new ResponseEntity<>(purchaseService.getPurchaseById(id, user), HttpStatus.CREATED);
    }

    @DeleteMapping("/purchases/{id}")
    public ResponseEntity<String> deletePurchase(@PathVariable("id") Long id,
                                            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userDetailsService.findByEmail(userDetails.getUsername());
        purchaseService.deletePurchaseById(id, user.getId());
        return new ResponseEntity<>("Purchase deleted", HttpStatus.NO_CONTENT);
    }



    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<String> handleRuntimeException(RuntimeException e) {
        System.out.println("purchase controller runtime exception kicked in");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }
}

