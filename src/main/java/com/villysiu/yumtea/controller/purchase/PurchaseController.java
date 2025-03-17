package com.villysiu.yumtea.controller.purchase;

import com.villysiu.yumtea.dto.request.PurchaseRequest;
import com.villysiu.yumtea.models.user.Account;

import com.villysiu.yumtea.dto.response.PurchaseProjection;
import com.villysiu.yumtea.service.AuthorizationService;
import com.villysiu.yumtea.service.PurchaseService;
import com.villysiu.yumtea.service.impl.CustomUserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class PurchaseController {

    private final PurchaseService purchaseService;
    private final AuthorizationService authorizationService;

    public PurchaseController(PurchaseService purchaseService, AuthorizationService authorizationService) {
        this.purchaseService = purchaseService;
        this.authorizationService = authorizationService;
    }


    @GetMapping("/purchases")
    public List<PurchaseProjection> getPurchases(@AuthenticationPrincipal UserDetails userDetails) {
        Account account = authorizationService.findByEmail(userDetails.getUsername());
         return purchaseService.getPurchasesByAccountId(account.getId());
    }

    @GetMapping("/purchases/{id}")
    public PurchaseProjection getPurchaseById(@PathVariable("id") Long id,
                                           @AuthenticationPrincipal UserDetails userDetails) {
        Account account = authorizationService.findByEmail(userDetails.getUsername());

        return purchaseService.getPurchaseById(id, account);
    }

    @PostMapping("/purchase")
    public ResponseEntity<PurchaseProjection> createPurchase(@RequestBody PurchaseRequest purchaseRequest, @AuthenticationPrincipal UserDetails userDetails) {
        Account account = authorizationService.findByEmail(userDetails.getUsername());
//        {"tip": 10.55, stata: "WA"}
        Long id = purchaseService.createPurchase(purchaseRequest, account);
        return new ResponseEntity<>(purchaseService.getPurchaseById(id, account), HttpStatus.CREATED);
    }

    @DeleteMapping("/purchases/{id}")
    public ResponseEntity<String> deletePurchase(@PathVariable("id") Long id, @AuthenticationPrincipal UserDetails userDetails) {
        Account account = authorizationService.findByEmail(userDetails.getUsername());

        try {
            purchaseService.deletePurchaseById(id, account);
            return new ResponseEntity<>("Purchase deleted", HttpStatus.NO_CONTENT);
        } catch(SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have permission to delete this cart.");
        } catch(UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Purchase not found.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }


}

