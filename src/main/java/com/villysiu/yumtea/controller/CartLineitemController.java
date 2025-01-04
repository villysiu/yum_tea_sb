package com.villysiu.yumtea.controller;

import com.villysiu.yumtea.dto.tea.CartLineitemDto;
import com.villysiu.yumtea.models.CartLineitem;
import com.villysiu.yumtea.models.user.User;
import com.villysiu.yumtea.projection.CartProjection;
import com.villysiu.yumtea.repo.CartLineitemRepo;
import com.villysiu.yumtea.repo.user.UserRepo;
import com.villysiu.yumtea.service.CartLineitemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CartLineitemController {
    private final CartLineitemService cartLineitemService;
    private final CartLineitemRepo cartLineitemRepo;
    private final UserRepo userRepo;


    @GetMapping("/cart")
    public List<CartProjection> getCartByUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        System.out.println(email);
        User user = userRepo.findByEmail(email);
        System.out.println(user);

        return cartLineitemRepo.findByUserId(user.getId());

    }
    //add one item to cart
    @PostMapping("/cart")
    public ResponseEntity<CartLineitem> addCart(@RequestBody CartLineitemDto cartLineitemDto) {
        System.out.println(cartLineitemDto);
        CartLineitem cartLineitem = cartLineitemService.createCartLineitem(cartLineitemDto);
        return new ResponseEntity<>(cartLineitem, HttpStatus.CREATED);
    }

    @PutMapping("/cart/{id}")
    public ResponseEntity<CartLineitem> updateCart(@PathVariable Long id, @RequestBody CartLineitemDto cartLineitemDto) {
return null;
    }


}
