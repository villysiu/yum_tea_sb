package com.villysiu.yumtea.controller.cart;

import com.villysiu.yumtea.dto.input.CartInputDto;
import com.villysiu.yumtea.dto.output.CartOutputDto;
import com.villysiu.yumtea.models.cart.Cart;
import com.villysiu.yumtea.models.user.User;
import com.villysiu.yumtea.projection.CartProjection;
import com.villysiu.yumtea.repo.cart.CartRepo;
import com.villysiu.yumtea.repo.user.UserRepo;
import com.villysiu.yumtea.service.CartService;
import com.villysiu.yumtea.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepo.findByEmail(email);
        System.out.println(user);

        return cartRepo.findByUserId(user.getId());

    }
    //add one item to cart
    @PostMapping("/cart")
    public ResponseEntity<CartOutputDto> addCart(@RequestBody CartInputDto cartLineitemDto) {
        System.out.println(cartLineitemDto);
        CartOutputDto cartLineitemOutputDto = cartService.createCart(cartLineitemDto);
        return new ResponseEntity<>(cartLineitemOutputDto, HttpStatus.CREATED);
    }

    @PutMapping("/cart/{id}")
    public ResponseEntity<CartOutputDto> updateCart(@PathVariable Long id, @RequestBody CartInputDto cartInputDto) {
        User currentUser = userService.getCurrentUser();
        Cart cart = cartRepo.findById(id).orElseThrow(()-> new RuntimeException("Cart not found."));
        System.out.println(currentUser.getRole());

//        if()
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        if(!cart.getUser().equals(userService.getCurrentUser())
            && !currentUser.getRole().name().equals("ADMIN"))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();


        CartOutputDto cartOutputDto = cartService.updateCart(id, cartInputDto);
        return new ResponseEntity<>(cartOutputDto, HttpStatus.CREATED);
    }
}
