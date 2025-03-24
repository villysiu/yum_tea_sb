package com.villysiu.yumtea.controller.purchase;

import com.villysiu.yumtea.dto.request.PurchaseRequest;
import com.villysiu.yumtea.models.user.Account;

import com.villysiu.yumtea.dto.response.PurchaseProjection;
import com.villysiu.yumtea.service.user.AuthorizationService;
import com.villysiu.yumtea.service.purchase.PurchaseService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
public class PurchaseController {

    private final PurchaseService purchaseService;
    private final AuthorizationService authorizationService;
    @Autowired
    public PurchaseController(PurchaseService purchaseService, AuthorizationService authorizationService) {
        this.purchaseService = purchaseService;
        this.authorizationService = authorizationService;
    }


    @GetMapping("/purchases")
    public List<PurchaseProjection> getPurchases(@AuthenticationPrincipal UserDetails userDetails) {
        Account account = authorizationService.findByEmail(userDetails.getUsername());
         return purchaseService.getPurchasesByAccountId(account.getId());
    }
    @GetMapping("/purchases/all")
    public List<PurchaseProjection> getPurchasesAll(@AuthenticationPrincipal UserDetails userDetails) {
        Account account = authorizationService.findByEmail(userDetails.getUsername());
        return purchaseService.getAllPurchases();
    }


    @GetMapping("/purchases/{id}")
    public PurchaseProjection getPurchaseById(@PathVariable("id") Long id, @AuthenticationPrincipal UserDetails userDetails) {
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

    @DeleteMapping("/purchase/{id}")
    public ResponseEntity<String> deletePurchase(@PathVariable("id") Long id, @AuthenticationPrincipal UserDetails userDetails) {

        Account account = authorizationService.findByEmail(userDetails.getUsername());

        purchaseService.deletePurchaseById(id, account);
        return new ResponseEntity<>("Purchase deleted", HttpStatus.NO_CONTENT);

    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " +e.getMessage());
    }
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
    @ExceptionHandler(SecurityException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<String> handleSecurityException(SecurityException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }


}

