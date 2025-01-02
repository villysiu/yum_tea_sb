package com.villysiu.yumtea.controller.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/resource")
public class AuthorizationController {
    @GetMapping
    public ResponseEntity<String> sayHello() {
        System.out.println("I am hjdhjfhjdshskr");
        return ResponseEntity.ok("Hello is the resource!");
    }
}
