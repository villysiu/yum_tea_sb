package com.villysiu.yumtea.service.impl;

import com.villysiu.yumtea.dto.request.SignupRequest;
import com.villysiu.yumtea.dto.request.SigninRequest;
import com.villysiu.yumtea.dto.response.SigninResponse;
import com.villysiu.yumtea.models.user.Account;
import com.villysiu.yumtea.models.user.Role;
import com.villysiu.yumtea.repo.user.RoleRepo;
import com.villysiu.yumtea.repo.user.AccountRepo;
import com.villysiu.yumtea.service.AuthenticationService;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;


@Service
public class AuthenticationServiceImpl implements AuthenticationService {


    private final AccountRepo accountRepo;
    private final RoleRepo roleRepo;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationServiceImpl(AccountRepo accountRepo, RoleRepo roleRepo, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.accountRepo = accountRepo;
        this.roleRepo = roleRepo;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

    @Override
    public Long signup(SignupRequest signupRequest) {
        logger.info("Signing up a new account with {}",signupRequest.getEmail());
        Optional<Account> dup = accountRepo.findByEmail(signupRequest.getEmail());
        if(dup.isPresent()){
            logger.error("Email already in use");
            throw new EntityExistsException("Email already exists");
        }

        Account account = new Account();
        account.setEmail(signupRequest.getEmail());
        account.setNickname(signupRequest.getNickname());
        account.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        logger.info("Assign Role_USER");
        Role role = roleRepo.findByName("ROLE_USER").orElseThrow(()->new EntityNotFoundException("Role not found"));

        account.setRoles(Collections.singleton(role));
        logger.info("Saving new account");
        accountRepo.save(account);
        logger.info("Saved new account");

        return account.getId();
    }



    @Override
    public SigninResponse signin(SigninRequest signinRequest, HttpServletRequest request) {
        logger.info("Signing in {}", signinRequest.getEmail());

        Authentication authenticationRequest = UsernamePasswordAuthenticationToken.unauthenticated(signinRequest.getEmail(), signinRequest.getPassword());
        Authentication authenticationResponse = this.authenticationManager.authenticate(authenticationRequest);
        logger.info("Email and Password authenticated");
//        If the credentials are correct, Spring Security creates an Authentication object (e.g., UsernamePasswordAuthenticationToken for form login or JwtAuthenticationToken for JWT-based login) and places it in the SecurityContext.
//        The authenticated Authentication object contains the user’s details, roles, and other information.
//            The Authentication contains:
//
//            principal: Identifies the user. When authenticating with a username/password this is often an instance of UserDetails.
//            System.out.println("principal: " + authenticationResponse.getPrincipal());
//            credentials: Often a password. In many cases, this is cleared after the user is authenticated, to ensure that it is not leaked.
//            System.out.println("cred: " + authenticationResponse.getCredentials());
//            authorities: The GrantedAuthority instances are high-level permissions the user is granted. Two examples are roles and scopes.
//            System.out.println("auth: " + authenticationResponse.getAuthorities());

//            principal: org.springframework.security.core.userdetails.User [Username=springuser@gg.com, Password=[PROTECTED], Enabled=true, AccountNonExpired=true, CredentialsNonExpired=true, AccountNonLocked=true, Granted Authorities=[ROLE_USER]]
//            cred: null
//            auth: [ROLE_USER]


        logger.info("Saving authenticated account to springsecurity");
         SecurityContextHolder.getContext().setAuthentication(authenticationResponse);

        logger.info("Saving authenticated account to httpsession");
        HttpSession session = request.getSession();
        session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());


        UserDetails userDetails = (UserDetails) authenticationResponse.getPrincipal();
        String email = userDetails.getUsername();
        Account account = accountRepo.findByEmail(email).orElseThrow(()->new EntityNotFoundException("email not found"));


        SigninResponse signinResponse = new SigninResponse();
        signinResponse.setId(account.getId());
        signinResponse.setEmail(account.getEmail());
        signinResponse.setNickname(account.getNickname());

        Role adminRole = roleRepo.findByName("ROLE_ADMIN").get();
        signinResponse.setIsAdmin(account.getRoles().contains(adminRole));
        logger.info("Return authenticated account in SigninResponse DTO");
        return signinResponse;


    }



}
