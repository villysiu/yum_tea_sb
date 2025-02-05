package com.villysiu.yumtea.controller.user;

import com.villysiu.yumtea.dto.request.SignupRequest;
import com.villysiu.yumtea.dto.request.SigninRequest;
import com.villysiu.yumtea.models.user.User;
import com.villysiu.yumtea.service.AuthenticationService;
import com.villysiu.yumtea.exception.EmailExistsException;
import jakarta.servlet.http.HttpServletRequest;
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
    public ResponseEntity<String> signup(@RequestBody SignupRequest request) {
        System.out.println("in signup");
        //SignupRequest{userName='spring', email='springuser@gg.com', password='password'}
        try{
            return authenticationService.signup(request);
        } catch (EmailExistsException e){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

    }

    @PostMapping("/signin")
    public ResponseEntity<String> signin(@RequestBody SigninRequest signinRequest, HttpServletRequest request) {
        System.out.println("in sign in controller");
        try{
            User user = authenticationService.signin(signinRequest, request );
            return new ResponseEntity<>(user.getEmail()+" signed in" ,HttpStatus.OK);


        } catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

    }
}