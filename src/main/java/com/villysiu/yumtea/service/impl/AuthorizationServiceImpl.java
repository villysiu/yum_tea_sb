package com.villysiu.yumtea.service.impl;

import com.villysiu.yumtea.dto.request.PasswordRequestDto;
import com.villysiu.yumtea.dto.response.SigninResponse;

import com.villysiu.yumtea.models.user.Account;
import com.villysiu.yumtea.repo.user.AccountRepo;
import com.villysiu.yumtea.service.AuthorizationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthorizationServiceImpl implements AuthorizationService {
    @Autowired
    private final AccountRepo accountRepo;
    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final AuthenticationManager authenticationManager;

    public AuthorizationServiceImpl(AccountRepo accountRepo, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.accountRepo = accountRepo;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }


    @Override
    public SigninResponse updateUser(Map<String, Object> userRequestDto, Account account) {
        for(Map.Entry<String, Object> entry : userRequestDto.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if(key.equals("nickname")) {
                account.setNickname((String) value);
            }
        }
        accountRepo.save(account);
        SigninResponse signinResponse = new SigninResponse();
        signinResponse.setEmail(account.getEmail());
        signinResponse.setNickname(account.getNickname());

        return signinResponse;
    }

    @Override
    public void updatePassword(PasswordRequestDto passwordRequestDto, Account account){
        String currentPassword = passwordRequestDto.getCurrentPassword();
        String newPassword = passwordRequestDto.getNewPassword();

        System.out.println(account.getPassword());
        System.out.println(currentPassword);

        Authentication authenticationRequest = UsernamePasswordAuthenticationToken.unauthenticated(account.getEmail(), currentPassword);
        Authentication authenticationResponse = this.authenticationManager.authenticate(authenticationRequest);

        System.out.println("authenticted!!");
        account.setPassword(passwordEncoder.encode(newPassword));
        accountRepo.save(account);

    }
}
