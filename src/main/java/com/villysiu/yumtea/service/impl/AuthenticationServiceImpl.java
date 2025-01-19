package com.villysiu.yumtea.service.impl;

import com.villysiu.yumtea.dto.request.SignupRequest;
import com.villysiu.yumtea.dto.request.SigninRequest;
import com.villysiu.yumtea.models.user.Role;
import com.villysiu.yumtea.models.user.User;
import com.villysiu.yumtea.repo.user.UserRepo;
import com.villysiu.yumtea.service.AuthenticationService;
import com.villysiu.yumtea.validation.EmailExistsException;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {


    private final UserRepo userRepo;

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;


    @Override
    public ResponseEntity<String> signup(SignupRequest request) throws EmailExistsException {

        if(userRepo.findByEmail(request.getEmail()) != null) {
            throw new EmailExistsException("Email already existed");
        }


        User user = new User();
        user.setUsername(request.getUserName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setRole(Role.USER);
        userRepo.save(user);

        return ResponseEntity.ok("Signup successful");
    }



    @Override
    public ResponseEntity<String> signin(SigninRequest request) {
        System.out.println("in AuthenticationServiceImpl signin");
        System.out.println(request.toString());
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        }
        catch (AuthenticationException e){
            System.out.println("failed?");
            throw new IllegalArgumentException("Invalid Email ot Password");
        }
        System.out.println("authenticated?");



        return ResponseEntity.ok("Signin successful");

    }



}
