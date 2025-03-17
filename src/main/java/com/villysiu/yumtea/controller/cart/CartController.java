package com.villysiu.yumtea.controller.cart;

import com.villysiu.yumtea.dto.request.CartInputDto;
import com.villysiu.yumtea.exception.EntityNotBelongToUserException;
import com.villysiu.yumtea.models.cart.Cart;
import com.villysiu.yumtea.models.user.Account;
import com.villysiu.yumtea.dto.response.CartProjection;
import com.villysiu.yumtea.service.AuthorizationService;
import com.villysiu.yumtea.service.CartService;

import com.villysiu.yumtea.service.impl.CustomUserDetailsServiceImpl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;


@RestController
public class CartController {

    private final CartService cartService;
    private final AuthorizationService authorizationService;

    @Autowired
    public CartController(CartService cartService,  AuthorizationService authorizationService) {
        this.cartService = cartService;
        this.authorizationService = authorizationService;
    }


    @GetMapping("/carts")
    public List<CartProjection> getCartsByAccount(@AuthenticationPrincipal UserDetails userDetails) {
        Account account = authorizationService.findByEmail(userDetails.getUsername());
        return cartService.getCartProjectionsByAccountId(account.getId());
    }
    // get input json  from frontend,
    // add into cart, or merge if already existed, return cart id
//    convert to cart projection and return with created status
    @PostMapping("/cart")
    public ResponseEntity<CartProjection> createCart(@RequestBody CartInputDto cartInputDto, @AuthenticationPrincipal UserDetails userDetails) {
        Account account = authorizationService.findByEmail(userDetails.getUsername());

        Long cartId = cartService.createCart(cartInputDto, account);
        return new ResponseEntity<>(cartService.getCartProjectionById(cartId), HttpStatus.CREATED);
    }


    @PutMapping("/cart/{id}")
    public ResponseEntity<CartProjection> updateCart(@PathVariable Long id, @RequestBody CartInputDto cartInputDto, @AuthenticationPrincipal UserDetails userDetails) {
        System.out.println("updating cart");
        Account account = authorizationService.findByEmail(userDetails.getUsername());

//        Cart cart = cartService.getCartById(id);
//
//        // only  owner of the cart can modify the cart , or ADMIN
//        if(!cart.getAccount().equals(account)) {
//            throw new EntityNotBelongToUserException("Cart does not belong to user");
//        }
//        Cart cart = cartService.getCartByIdAndAccountId(id, account.getId());

        Long cartId = cartService.updateCart(id, cartInputDto, account);
        return new ResponseEntity<>(cartService.getCartProjectionById(cartId), HttpStatus.CREATED);

    }

    //Not doing patch to avoid complicated calculation add this and minus the previous etc

    @DeleteMapping("/cart/{id}")
    public ResponseEntity<String> deleteCart(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id) {
        Account account = authorizationService.findByEmail(userDetails.getUsername());

        try {
            cartService.deleteCartById(id, account);
            return new ResponseEntity<>("Cart deleted", HttpStatus.NO_CONTENT);
        } catch(SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have permission to delete this cart.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }

    }
}

