package com.villysiu.yumtea.service.impl;

import com.villysiu.yumtea.dao.request.SignupRequest;
import com.villysiu.yumtea.dao.response.JwtAuthenticationResponse;
import com.villysiu.yumtea.dao.request.SigninRequest;
import com.villysiu.yumtea.models.user.Role;
import com.villysiu.yumtea.models.user.User;
import com.villysiu.yumtea.repo.user.UserRepo;
import com.villysiu.yumtea.service.AuthenticationService;
import com.villysiu.yumtea.service.JwtService;
import com.villysiu.yumtea.validation.EmailExistsException;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {


    private final UserRepo userRepo;

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;


    @Override
    public JwtAuthenticationResponse signup(SignupRequest request) throws EmailExistsException {

        if(userRepo.findByEmail(request.getEmail()).isPresent()){
            throw new EmailExistsException("Email already existed");
        }
        System.out.println("I am AuthenticationServiceImpl");

        User user = new User();
        user.setUsername(request.getUserName());
        user.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setRole(Role.USER);
        userRepo.save(user);

        String jwt = jwtService.generateToken(user);
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }



    @Override
    public JwtAuthenticationResponse signin(SigninRequest request) {
        System.out.println("in JwtAuthenticationResponse signin");
        System.out.println(request.toString());
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        System.out.println("authenticated?");


        Optional<User> user = userRepo.findByEmail(request.getEmail());
        if(user.isPresent()){
            System.out.println("user is here");
            String jwt = jwtService.generateToken(user.get());

            return JwtAuthenticationResponse.builder().token(jwt).build();
        }
        else {
            throw new IllegalArgumentException("Invalid Email ot Password");
        }

    }



}
