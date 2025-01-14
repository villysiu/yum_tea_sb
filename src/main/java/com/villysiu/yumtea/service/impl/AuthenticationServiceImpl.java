package com.villysiu.yumtea.service.impl;

import com.villysiu.yumtea.dto.request.SignupRequest;
import com.villysiu.yumtea.dao.response.JwtAuthenticationResponse;
import com.villysiu.yumtea.dto.request.SigninRequest;
import com.villysiu.yumtea.models.user.Role;
import com.villysiu.yumtea.models.user.User;
import com.villysiu.yumtea.repo.user.UserRepo;
import com.villysiu.yumtea.service.AuthenticationService;
import com.villysiu.yumtea.service.JwtService;
import com.villysiu.yumtea.validation.EmailExistsException;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {


    private final UserRepo userRepo;
    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;


    @Override
    public JwtAuthenticationResponse signup(SignupRequest request) throws EmailExistsException {

        if(userRepo.findByEmail(request.getEmail()) != null) {
            throw new EmailExistsException("Email already existed");
        }


        User user = new User();
        user.setUsername(request.getUserName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
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
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        }
        catch (AuthenticationException e){
            System.out.println("failed?");
            throw new IllegalArgumentException("Invalid Email ot Password");
        }
        System.out.println("authenticated?");


        User user = userRepo.findByEmail(request.getEmail());

        String jwt = jwtService.generateToken(user);

        return JwtAuthenticationResponse.builder().token(jwt).build();

    }



}
