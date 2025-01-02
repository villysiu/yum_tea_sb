package com.villysiu.yumtea.controller.user;

import com.villysiu.yumtea.dao.request.SignupRequest;
import com.villysiu.yumtea.dao.response.JwtAuthenticationResponse;
import com.villysiu.yumtea.dao.request.SigninRequest;
import com.villysiu.yumtea.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<JwtAuthenticationResponse> signup(@RequestBody SignupRequest request) {
        System.out.println("I am in signup");
        //SignupRequest{userName='spring', email='springuser@gg.com', password='password'}

//        return null;
        return ResponseEntity.ok(authenticationService.signup(request));
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> signin(@RequestBody SigninRequest request) {
        System.out.println("I am in SIGNIN");
        //SignupRequest{email='springuser@gg.com', password='password'}
        System.out.println(request.toString());
//        return null;
        return ResponseEntity.ok(authenticationService.signin(request));
    }
}