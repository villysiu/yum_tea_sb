package com.villysiu.yumtea.controller.cart;

import com.villysiu.yumtea.dto.request.CartInputDto;
import com.villysiu.yumtea.models.user.Account;
import com.villysiu.yumtea.dto.response.CartProjection;
import com.villysiu.yumtea.service.user.AuthorizationService;
import com.villysiu.yumtea.service.cart.CartService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
public class CartController {

    private final CartService cartService;
    private final AuthorizationService authorizationService;

    @Autowired
    public CartController(CartService cartService,  AuthorizationService authorizationService) {
        this.cartService = cartService;
        this.authorizationService = authorizationService;
    }

// authenticated user can get his carts
    @GetMapping("/carts")
    public List<CartProjection> getCartsByAccount(@AuthenticationPrincipal UserDetails userDetails) {
        Account account = authorizationService.findByEmail(userDetails.getUsername());
        return cartService.getCartProjectionsByAccountId(account.getId());
    }

    // authenticated user use this to  create single  cart
    // no admin
    @PostMapping("/cart")
    public ResponseEntity<CartProjection> createCart(@RequestBody CartInputDto cartInputDto, @AuthenticationPrincipal UserDetails userDetails) {
        Account account = authorizationService.findByEmail(userDetails.getUsername());

        Long cartId = cartService.createCart(cartInputDto, account);
        return new ResponseEntity<>(cartService.getCartProjectionById(cartId), HttpStatus.CREATED);
    }

    //  authenticated user use this to  update his own single cart
    // no admin
    @PutMapping("/cart/{id}")
    public ResponseEntity<CartProjection> updateCart(@PathVariable Long id, @RequestBody CartInputDto cartInputDto, @AuthenticationPrincipal UserDetails userDetails) {
        Account account = authorizationService.findByEmail(userDetails.getUsername());

        Long cartId = cartService.updateCart(id, cartInputDto, account);
        return new ResponseEntity<>(cartService.getCartProjectionById(cartId), HttpStatus.CREATED);

    }

    //Not doing patch to avoid complicated calculation add this and minus the previous etc
    // authenticated user use this to  delete his own single cart
    // admin can delete
    @DeleteMapping("/cart/{id}")
    public ResponseEntity<String> deleteCart(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id) {
        Account account = authorizationService.findByEmail(userDetails.getUsername());

        try {
            cartService.deleteCartById(id, account);
            return new ResponseEntity<>("Cart deleted", HttpStatus.NO_CONTENT);
        } catch(SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have permission to delete this cart.");
        }
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " +e.getMessage());
    }
}

