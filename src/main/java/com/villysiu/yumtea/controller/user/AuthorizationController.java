package com.villysiu.yumtea.controller.user;

import com.villysiu.yumtea.models.user.User;
import com.villysiu.yumtea.repo.user.UserRepo;
import com.villysiu.yumtea.service.UserService;
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
    private final UserService userService;
    @GetMapping
    public ResponseEntity<String> sayHello() {
        System.out.println("I am in a secured resource");
        return ResponseEntity.ok("Hello is the resource!");
    }

    @GetMapping("/user")
    public User getCurrentUser() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        return "User Details: " + authentication.getName();
        return userService.getCurrentUser();
    }


}
