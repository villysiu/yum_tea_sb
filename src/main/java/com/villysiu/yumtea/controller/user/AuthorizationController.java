package com.villysiu.yumtea.controller.user;

import com.villysiu.yumtea.repo.user.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/resource")
public class AuthorizationController {
    private final UserRepo userRepo;
    @GetMapping
    public ResponseEntity<String> sayHello() {
        System.out.println("I am in a secured resource");
        return ResponseEntity.ok("Hello is the resource!");
    }
    @GetMapping("/user")
    public String getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return "User Details: " + authentication.getName();
    }


}
