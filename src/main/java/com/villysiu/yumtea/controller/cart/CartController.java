package com.villysiu.yumtea.controller.cart;

import com.villysiu.yumtea.dto.request.CartInputDto;
import com.villysiu.yumtea.models.cart.Cart;
import com.villysiu.yumtea.models.user.Role;
import com.villysiu.yumtea.models.user.User;
import com.villysiu.yumtea.dao.CartProjection;
import com.villysiu.yumtea.service.CartService;

import com.villysiu.yumtea.service.impl.CustomUserDetailsServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import lombok.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


@RestController
public class CartController {
    @Autowired
    private final CartService cartService;

    @Autowired
    private final CustomUserDetailsServiceImpl userDetailsService;

    public CartController(CartService cartService, CustomUserDetailsServiceImpl userDetailsService) {
        this.cartService = cartService;
        this.userDetailsService = userDetailsService;
    }


    @GetMapping("cartsByQuery")
    public List<Object[]> getCartsByUserQuery(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userDetailsService.findByEmail(userDetails.getUsername());
        return cartService.getCartsByUserQuery(user);

    }
    @GetMapping("/cartsByProjection")
    public List<CartProjection> getCartProjectionByUser(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userDetailsService.findByEmail(userDetails.getUsername());
        return cartService.getCartProjectionsByUserId(user.getId());
    }

    // get input json  from frontend,
    // add into cart, or merge if already existed, return cart id
//    convert to cart projection and return with created status
    @PostMapping("/cart")
    public ResponseEntity<CartProjection> addCart(@RequestBody CartInputDto cartInputDto, @AuthenticationPrincipal UserDetails userDetails) {
        System.out.println(userDetails);
        User user = userDetailsService.findByEmail(userDetails.getUsername());

        Long cartId = cartService.createCart(cartInputDto, user);
        return new ResponseEntity<>(cartService.getCartProjectionById(cartId), HttpStatus.CREATED);
    }


    @PutMapping("/cart/{id}")
    public ResponseEntity<CartProjection> updateCart(@PathVariable Long id, @RequestBody CartInputDto cartInputDto, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userDetailsService.findByEmail(userDetails.getUsername());

        Cart cart = cartService.getCartById(id);

        // only  owner of the cart can modify the cart , or ADMIN
        if(!cart.getUser().equals(user) && !userDetailsService.isAdmin(user)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Long cartId = cartService.updateCart(id, cartInputDto, user);
        return new ResponseEntity<>(cartService.getCartProjectionById(cartId), HttpStatus.CREATED);

    }

    //Not doing patch to avoid complicated calculation add this and minus the previous etc

    @DeleteMapping("/cart/{id}")
    public ResponseEntity<Long> deleteCart(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id) {
        User user = userDetailsService.findByEmail(userDetails.getUsername());
        Cart cart = cartService.getCartById(id);


        if(!cart.getUser().equals(user) && !userDetailsService.isAdmin(user)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        cartService.deleteCartById(cart.getId());
        return new ResponseEntity<>(id, HttpStatus.NO_CONTENT);
    }
    // Custom exception handler when no cart found with id.
//    @ExceptionHandler(NoSuchElementException.class)
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    public ResponseEntity<String> handleNoSuchElementException(NoSuchElementException e) {
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
