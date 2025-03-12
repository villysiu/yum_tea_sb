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
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private final AccountRepo accountRepo;
    @Autowired
    private final RoleRepo roleRepo;
    @Autowired
    private final AuthenticationManager authenticationManager;
    @Autowired
    private final PasswordEncoder passwordEncoder;


    @Override
    public Long signup(SignupRequest signupRequest) {
        Optional<Account> dup = accountRepo.findByEmail(signupRequest.getEmail());
        if(dup.isPresent()){
            throw new EntityExistsException("Email already exists");
        }

        Account account = new Account();
        account.setEmail(signupRequest.getEmail());
        account.setNickname(signupRequest.getNickname());
        account.setPassword(passwordEncoder.encode(signupRequest.getPassword()));

        Role role = roleRepo.findByName("ROLE_USER").orElseThrow(()->new EntityNotFoundException("Role not found"));

        account.setRoles(Collections.singleton(role));
        accountRepo.save(account);

        return account.getId();
    }



    @Override
    public SigninResponse signin(SigninRequest signinRequest, HttpServletRequest request) {
        System.out.println("in AuthenticationServiceImpl signin");
        System.out.println(signinRequest.toString());

            Authentication authenticationRequest = UsernamePasswordAuthenticationToken.unauthenticated(signinRequest.getEmail(), signinRequest.getPassword());
            Authentication authenticationResponse = this.authenticationManager.authenticate(authenticationRequest);

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

             SecurityContextHolder.getContext().setAuthentication(authenticationResponse);


            HttpSession session = request.getSession();
            session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());


            UserDetails userDetails = (UserDetails) authenticationResponse.getPrincipal();
            String email = userDetails.getUsername();
            System.out.println("email: " + email);
            Account account = accountRepo.findByEmail(email).orElseThrow(()->new EntityNotFoundException("email not found"));

            SigninResponse signinResponse = new SigninResponse();
            signinResponse.setEmail(account.getEmail());
            signinResponse.setNickname(account.getNickname());

            return signinResponse;


    }

    @Override
    public SigninResponse adminSignin(SigninRequest signinRequest, HttpServletRequest request) {
        System.out.println("in admin signin");


        Authentication authenticationRequest = UsernamePasswordAuthenticationToken.unauthenticated(signinRequest.getEmail(), signinRequest.getPassword());
        Authentication authenticationResponse = this.authenticationManager.authenticate(authenticationRequest);

//            The Authentication contains:
//
//            principal: Identifies the user. When authenticating with a username/password this is often an instance of UserDetails.
//            System.out.println("principal: " + authenticationResponse.getPrincipal());
//            credentials: Often a password. In many cases, this is cleared after the user is authenticated, to ensure that it is not leaked.
//            System.out.println("cred: " + authenticationResponse.getCredentials());
//            authorities: The GrantedAuthority instances are high-level permissions the user is granted. Two examples are roles and scopes.
            System.out.println("auth: " + authenticationResponse.getAuthorities());

//            principal: org.springframework.security.core.userdetails.User [Username=springuser@gg.com, Password=[PROTECTED], Enabled=true, AccountNonExpired=true, CredentialsNonExpired=true, AccountNonLocked=true, Granted Authorities=[ROLE_USER]]
//            cred: null
//            auth: [ROLE_USER]
        Role adminRole = roleRepo.findByName("ROLE_ADMIN").get();

        if(authenticationResponse.getAuthorities().stream()
                .noneMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(adminRole.getName()))){
            System.out.println("not a admin");
            throw new EntityNotFoundException("User is not Admin");
        }

        SecurityContextHolder.getContext().setAuthentication(authenticationResponse);

        HttpSession session = request.getSession();
        session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());


        UserDetails userDetails = (UserDetails) authenticationResponse.getPrincipal();
        String email = userDetails.getUsername();
        System.out.println("email: " + email);
        Account account = accountRepo.findByEmail(email).orElseThrow(()->new EntityNotFoundException("email not found"));

        SigninResponse signinResponse = new SigninResponse();
        signinResponse.setEmail(account.getEmail());
        signinResponse.setNickname(account.getNickname());

        return signinResponse;


    }

}
