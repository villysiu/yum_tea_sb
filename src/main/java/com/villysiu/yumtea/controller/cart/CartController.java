package com.villysiu.yumtea.controller.cart;

import com.villysiu.yumtea.dto.request.CartInputDto;
import com.villysiu.yumtea.models.cart.Cart;
import com.villysiu.yumtea.models.user.User;
import com.villysiu.yumtea.projection.CartProjection;
import com.villysiu.yumtea.service.CartService;
import com.villysiu.yumtea.service.UserService;

import lombok.*;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Data
@RequiredArgsConstructor
@RestController
public class CartController {
    private final CartService cartService;
//    private final CartRepo cartRepo;
    private final UserService userService;


    @GetMapping("/cart")
    public List<CartProjection> getCartByUser() {
        User currentUser = userService.getCurrentUser();
        return cartService.getCartProjectionsByUserId(currentUser.getId());

    }

    // get input json  from frontend,
    // add into cart, or merge if already existed, return cart id
//    convert to cart projection and return with created status
    @PostMapping("/cart")
    public ResponseEntity<CartProjection> addCart(@RequestBody CartInputDto cartInputDto) {

        Long cartId = cartService.createCart(cartInputDto);
        return new ResponseEntity<>(cartService.getCartProjectionById(cartId), HttpStatus.CREATED);
    }


    @PutMapping("/cart/{id}")
    public ResponseEntity<CartProjection> updateCart(@PathVariable Long id, @RequestBody CartInputDto cartInputDto) {
        Cart cart = cartService.getCartById(id);

        User currentUser = userService.getCurrentUser();

        System.out.println(currentUser.getRole());

        // only  owner of the cart can modify the cart , or ADMIN
        if(!cart.getUser().equals(currentUser)
            && !currentUser.getRole().name().equals("ADMIN"))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();


        Long cartId = cartService.updateCart(id, cartInputDto);
        return new ResponseEntity<>(cartService.getCartProjectionById(cartId), HttpStatus.CREATED);
    }

    //Not doing patch to avoid complicated calculation add this and minus the previous etc
}
