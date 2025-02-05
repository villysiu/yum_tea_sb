package com.villysiu.yumtea.controller.user;

import com.villysiu.yumtea.models.user.User;
import com.villysiu.yumtea.repo.user.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/resource")
public class AuthorizationController {


    @GetMapping
    public ResponseEntity<String> sayHello() {
        System.out.println("I am in a secured resource");
        return ResponseEntity.ok("Hello is the resource!");
    }

    @GetMapping("/user")
    public User getCurrentUser(@AuthenticationPrincipal User user) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        return "User Details: " + authentication.getName();
        return user;
    }



}
