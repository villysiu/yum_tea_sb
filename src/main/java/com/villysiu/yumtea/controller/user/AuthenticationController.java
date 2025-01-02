package com.villysiu.yumtea.controller.user;

import com.villysiu.yumtea.dao.request.SignupRequest;
import com.villysiu.yumtea.dao.response.JwtAuthenticationResponse;
import com.villysiu.yumtea.dao.request.SigninRequest;
import com.villysiu.yumtea.service.AuthenticationService;
import com.villysiu.yumtea.validation.EmailExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
        try{
            return ResponseEntity.ok(authenticationService.signup(request));
        } catch (EmailExistsException e){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

    }

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> signin(@RequestBody SigninRequest request) {
        try{

            return ResponseEntity.ok(authenticationService.signin(request));

        } catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

    }
}