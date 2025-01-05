package com.villysiu.yumtea.controller.cart;

import com.villysiu.yumtea.dto.input.CartInputDto;
import com.villysiu.yumtea.models.cart.Cart;
import com.villysiu.yumtea.models.user.User;
import com.villysiu.yumtea.projection.CartProjection;
import com.villysiu.yumtea.repo.cart.CartRepo;
import com.villysiu.yumtea.repo.user.UserRepo;
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
    private final CartRepo cartRepo;
    private final UserRepo userRepo;
    private final UserService userService;


    @GetMapping("/cart")
    public List<CartProjection> getCartByUser() {
        User currentUser = userService.getCurrentUser();

        return cartRepo.findByUser(currentUser);

    }

    // get input json  from frontend,
    // add into cart, or merge if already existed, return cart id
//    convert to cart projection and return with created status
    @PostMapping("/cart")
    public ResponseEntity<CartProjection> addCart(@RequestBody CartInputDto cartInputDto) {

        Long cartId = cartService.createCart(cartInputDto);
        return new ResponseEntity<>(cartRepo.findCartById(cartId), HttpStatus.CREATED);
    }


    @PutMapping("/cart/{id}")
    public ResponseEntity<CartProjection> updateCart(@PathVariable Long id, @RequestBody CartInputDto cartInputDto) {
        User currentUser = userService.getCurrentUser();
        Cart cart = cartRepo.findById(id).orElseThrow(()-> new RuntimeException("Cart not found."));
        System.out.println(currentUser.getRole());

        // only  owner of the cart can modify the cart , or ADMIN
        if(!cart.getUser().equals(userService.getCurrentUser())
            && !currentUser.getRole().name().equals("ADMIN"))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();


        Long cartId = cartService.updateCart(id, cartInputDto);
        return new ResponseEntity<>(cartRepo.findCartById(cartId), HttpStatus.CREATED);
    }
}
