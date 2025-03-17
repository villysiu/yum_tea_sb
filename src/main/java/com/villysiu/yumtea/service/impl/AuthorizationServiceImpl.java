package com.villysiu.yumtea.service.impl;

import com.villysiu.yumtea.dto.request.PasswordRequestDto;
import com.villysiu.yumtea.dto.response.SigninResponse;

import com.villysiu.yumtea.models.user.Account;
import com.villysiu.yumtea.models.user.Role;
import com.villysiu.yumtea.repo.user.AccountRepo;
import com.villysiu.yumtea.repo.user.RoleRepo;
import com.villysiu.yumtea.service.AuthorizationService;

import com.villysiu.yumtea.service.CartService;
import com.villysiu.yumtea.service.PurchaseService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthorizationServiceImpl implements AuthorizationService {

    private final AccountRepo accountRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final RoleRepo roleRepo;
    private final CartService cartService;
    private final PurchaseService purchaseService;

    public AuthorizationServiceImpl(AccountRepo accountRepo, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, RoleRepo roleRepo, CartService cartService, PurchaseService purchaseService) {
        this.accountRepo = accountRepo;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.roleRepo = roleRepo;
        this.cartService = cartService;
        this.purchaseService = purchaseService;
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

    @Override
    public List<SigninResponse> getAllAccounts() {

        Role adminRole = roleRepo.findByName("ROLE_ADMIN").get();
        List<SigninResponse> accounts = new ArrayList<>();

        for(Account account: accountRepo.findAll()){
            SigninResponse signinResponse = new SigninResponse();
            signinResponse.setId(account.getId());
            signinResponse.setEmail(account.getEmail());
            signinResponse.setNickname(account.getNickname());

            signinResponse.setIsAdmin(account.getRoles().contains(adminRole));

            accounts.add(signinResponse);
        }
        return accounts;
    }

    @Override
    public SigninResponse toggleAdminRole(Long id) {
        Account account = accountRepo.findById(id).orElseThrow(()->new EntityNotFoundException("Account not found"));
        Role adminRole = roleRepo.findByName("ROLE_ADMIN").get();

        if(account.getRoles().contains(adminRole)){
            account.getRoles().remove(adminRole);
        }
        else{
            account.getRoles().add(adminRole);
        }
//        System.out.println(account.getRoles());
        accountRepo.save(account);


        SigninResponse signinResponse = new SigninResponse();
        signinResponse.setEmail(account.getEmail());
        signinResponse.setNickname(account.getNickname());
        signinResponse.setIsAdmin(account.getRoles().contains(adminRole));
        return signinResponse;
    }

    @Transactional
    @Override
    public void deleteAccount(Long id) {
        Account account = accountRepo.findById(id).orElseThrow(()->new EntityNotFoundException("Account not found"));
        cartService.deleteCartsByAccountId(id, account);
        purchaseService.deleteAllPurchasesByAccountId(account.getId());
        accountRepo.delete(account);
    }

    public boolean isAdmin(Account account) {
        Role adminRole = roleRepo.findByName("ROLE_ADMIN").get();
//        Role adminRole = roleRepo.findByName("ROLE_ADMIN").orElse(new Role("ROLE_ADMIN"));
        return account.getRoles().contains(adminRole);
    }
}

