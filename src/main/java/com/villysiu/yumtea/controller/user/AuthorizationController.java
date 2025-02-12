package com.villysiu.yumtea.controller.user;

import com.villysiu.yumtea.models.user.User;
import com.villysiu.yumtea.repo.user.UserRepo;
import com.villysiu.yumtea.service.impl.CustomUserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/resource")
public class AuthorizationController {

    @Autowired
    private final CustomUserDetailsServiceImpl userDetailsService;

    public AuthorizationController(CustomUserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @GetMapping
    public ResponseEntity<String> sayHello() {
        System.out.println("I am in a secured resource");
        return ResponseEntity.ok("Hello is the resource!");
    }

    @GetMapping("/user")
    public User getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userDetailsService.findByEmail(userDetails.getUsername());
        return user;
    }



}
