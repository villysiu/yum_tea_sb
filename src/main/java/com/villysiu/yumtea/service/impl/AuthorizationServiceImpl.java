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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
public class AuthorizationServiceImpl implements AuthorizationService {

    private final AccountRepo accountRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final RoleService roleService;
    private final CartService cartService;
    private final PurchaseService purchaseService;


    @Autowired
    public AuthorizationServiceImpl(AccountRepo accountRepo, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, RoleService roleService
            , CartService cartService, PurchaseService purchaseService
    ) {
        this.accountRepo = accountRepo;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.roleService = roleService;
        this.cartService = cartService;
        this.purchaseService = purchaseService;
    }
    private static final Logger logger = LoggerFactory.getLogger(AuthorizationServiceImpl.class);


    @Override
    public SigninResponse updateUser(Map<String, Object> userRequestDto, Account account) {
        for(Map.Entry<String, Object> entry : userRequestDto.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if(key.equals("nickname")) {

                account.setNickname((String) value);
            }
        }
        logger.info("saving account");
        accountRepo.save(account);
        logger.info("Account successfully saved");
        SigninResponse signinResponse = new SigninResponse();
        signinResponse.setEmail(account.getEmail());
        signinResponse.setNickname(account.getNickname());

        return signinResponse;
    }

    @Override
    public void updatePassword(PasswordRequestDto passwordRequestDto, Account account){
        String currentPassword = passwordRequestDto.getCurrentPassword();
        String newPassword = passwordRequestDto.getNewPassword();

        logger.info("Authenticating account");
        Authentication authenticationRequest = UsernamePasswordAuthenticationToken.unauthenticated(account.getEmail(), currentPassword);
        Authentication authenticationResponse = this.authenticationManager.authenticate(authenticationRequest);

        logger.info("Authenticated");

        account.setPassword(passwordEncoder.encode(newPassword));

        logger.info("saving account");
        accountRepo.save(account);
        logger.info("Account successfully saved");
    }

    @Override
    public List<SigninResponse> getAllAccounts() {

        Role adminRole = roleService.getRoleByName("ROLE_ADMIN");

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
        Account account = accountRepo.findById(id).orElseThrow(()->new UsernameNotFoundException("User not found"));
        Role adminRole = roleService.getRoleByName("ROLE_ADMIN");
        logger.info("toggling admin role");
        if(account.getRoles().contains(adminRole)){
            account.getRoles().remove(adminRole);
        }
        else{
            account.getRoles().add(adminRole);
        }
//        System.out.println(account.getRoles());
        logger.info("Saving account");
        accountRepo.save(account);
        logger.info("Account saved");

        SigninResponse signinResponse = new SigninResponse();
        signinResponse.setEmail(account.getEmail());
        signinResponse.setNickname(account.getNickname());
        signinResponse.setIsAdmin(account.getRoles().contains(adminRole));
        return signinResponse;
    }

    @Transactional
    @Override
    public void deleteAccount(Long id, Account authenticatedAccount) {

        Account deleteAccount = accountRepo.findById(id).orElseThrow(()->new EntityNotFoundException("Account not found"));
        if (roleService.isAdmin(authenticatedAccount) || deleteAccount == authenticatedAccount) {
            try {
                logger.info("Deleting Carts related to Account {}", id);
                cartService.deleteCartsByAccountId(id, authenticatedAccount);

                logger.info("Deleting Purchases related to Account {}", id);
                purchaseService.deletePurchasesByAccountId(id, authenticatedAccount);

                logger.info("Deleting Account {}", id);
                accountRepo.delete(deleteAccount);
                logger.info("Successfully deleting Account {}", id);
            }  catch(Exception e) {
                logger.error("Error occurred while deleting carts: " + e.getMessage(), e);
                throw new RuntimeException("Error occurred while deleting purchases", e);
            }
        } else {
            logger.error("You do not have permission to delete Account {}", id);
            throw new SecurityException("You do not have permission to delete purchases.");
        }


    }


    @Override
    public Account findByEmail(String email) {
        return accountRepo.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email + " not found."));
    }
}

