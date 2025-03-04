package com.villysiu.yumtea.service.impl;

import com.villysiu.yumtea.dto.request.PasswordRequestDto;
import com.villysiu.yumtea.dto.response.SigninResponse;
import com.villysiu.yumtea.models.user.User;
import com.villysiu.yumtea.repo.user.UserRepo;
import com.villysiu.yumtea.service.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthorizationServiceImpl implements AuthorizationService {
    @Autowired
    private final UserRepo userRepo;
    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final AuthenticationManager authenticationManager;

    public AuthorizationServiceImpl(UserRepo userRepo, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }


    @Override
    public SigninResponse updateUser(Map<String, Object> userRequestDto, User user) {
        for(Map.Entry<String, Object> entry : userRequestDto.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if(key.equals("nickname")) {
                user.setNickname((String) value);
            }
        }
        userRepo.save(user);
        SigninResponse signinResponse = new SigninResponse();
        signinResponse.setEmail(user.getEmail());
        signinResponse.setNickname(user.getNickname());

        return signinResponse;
    }

    @Override
    public void updatePassword(PasswordRequestDto passwordRequestDto, User user){
        String currentPassword = passwordRequestDto.getCurrentPassword();
        String newPassword = passwordRequestDto.getNewPassword();

        System.out.println(user.getPassword());
        System.out.println(currentPassword);
//try{
            Authentication authenticationRequest = UsernamePasswordAuthenticationToken.unauthenticated(user.getEmail(), currentPassword);
            Authentication authenticationResponse = this.authenticationManager.authenticate(authenticationRequest);

            System.out.println("authenticted!!");
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepo.save(user);
//        }
//        catch (AuthenticationException e){
//            System.out.println(e.getMessage());
//            throw new IllegalArgumentException("Password not matched");
//        }
    }
    }
