package com.villysiu.yumtea.controller.cart;

import com.villysiu.yumtea.dto.request.CartInputDto;
import com.villysiu.yumtea.exception.EntityNotBelongToUserException;
import com.villysiu.yumtea.models.cart.Cart;
import com.villysiu.yumtea.models.user.Account;
import com.villysiu.yumtea.dto.response.CartProjection;
import com.villysiu.yumtea.service.CartService;

import com.villysiu.yumtea.service.impl.CustomUserDetailsServiceImpl;

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
    private final CustomUserDetailsServiceImpl userDetailsService;

    public CartController(CartService cartService, CustomUserDetailsServiceImpl userDetailsService) {
        this.cartService = cartService;
        this.userDetailsService = userDetailsService;
    }


//    @GetMapping("/cartsByProjection")
//    public List<CartProjection> getCartProjectionsByUser(@AuthenticationPrincipal UserDetails userDetails) {
//        User user = userDetailsService.findByEmail(userDetails.getUsername());
//        return cartService.getCartProjectionsByUserId(user.getId());
//    }
    @GetMapping("/carts")
    public List<CartProjection> getCartsByUser(@AuthenticationPrincipal UserDetails userDetails) {
        Account account = userDetailsService.findByEmail(userDetails.getUsername());
        return cartService.getCartProjectionsByUserId(account.getId());
    }
    // get input json  from frontend,
    // add into cart, or merge if already existed, return cart id
//    convert to cart projection and return with created status
    @PostMapping("/cart")
    public ResponseEntity<CartProjection> createCart(@RequestBody CartInputDto cartInputDto, @AuthenticationPrincipal UserDetails userDetails) {
        Account account = userDetailsService.findByEmail(userDetails.getUsername());

        Long cartId = cartService.createCart(cartInputDto, account);
        return new ResponseEntity<>(cartService.getCartProjectionById(cartId), HttpStatus.CREATED);
    }


    @PutMapping("/cart/{id}")
    public ResponseEntity<CartProjection> updateCart(@PathVariable Long id, @RequestBody CartInputDto cartInputDto, @AuthenticationPrincipal UserDetails userDetails) {
        System.out.println("updating cart");
        Account account = userDetailsService.findByEmail(userDetails.getUsername());

        Cart cart = cartService.getCartById(id);

        // only  owner of the cart can modify the cart , or ADMIN
        if(!cart.getAccount().equals(account)) {
            throw new EntityNotBelongToUserException("Cart does not belong to user");
        }

        Long cartId = cartService.updateCart(id, cartInputDto, account);
        return new ResponseEntity<>(cartService.getCartProjectionById(cartId), HttpStatus.CREATED);

    }

    //Not doing patch to avoid complicated calculation add this and minus the previous etc

    @DeleteMapping("/cart/{id}")
    public ResponseEntity<String> deleteCart(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id) {
        Account account = userDetailsService.findByEmail(userDetails.getUsername());
        Cart cart = cartService.getCartById(id);

        if(!cart.getAccount().equals(account)) {
            throw new EntityNotBelongToUserException("Cart does not belong to user");
        }
        cartService.deleteCartById(cart.getId());
        return new ResponseEntity<>("Cart deleted", HttpStatus.NO_CONTENT);
    }

    // Custom exception handler when no cart found with id.
    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleNoSuchElementException(NoSuchElementException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<String> handleRuntimeException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

//    @ExceptionHandler(HttpMessageNotReadableException.class)
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//    }
}
