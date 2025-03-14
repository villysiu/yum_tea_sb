package com.villysiu.yumtea.controller.user;

import com.villysiu.yumtea.dto.request.PasswordRequestDto;
import com.villysiu.yumtea.dto.response.SigninResponse;

import com.villysiu.yumtea.models.user.Account;
import com.villysiu.yumtea.models.user.Role;
import com.villysiu.yumtea.repo.user.RoleRepo;
import com.villysiu.yumtea.service.AuthorizationService;
import com.villysiu.yumtea.service.impl.CustomUserDetailsServiceImpl;
import com.villysiu.yumtea.service.impl.RoleService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/resource")
public class AuthorizationController {

    private final CustomUserDetailsServiceImpl userDetailsService;
    private final AuthorizationService authorizationService;
    private final RoleRepo roleRepo;
//    private final RoleRepo roleRepo;

    public AuthorizationController(CustomUserDetailsServiceImpl userDetailsService, AuthorizationService authorizationService, RoleRepo roleRepo) {
        this.userDetailsService = userDetailsService;
        this.authorizationService = authorizationService;
        this.roleRepo = roleRepo;
    }


    @GetMapping("/user")
    public ResponseEntity<SigninResponse> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        Account account = userDetailsService.findByEmail(userDetails.getUsername());

        SigninResponse signinResponse = new SigninResponse();
        signinResponse.setEmail(account.getEmail());
        signinResponse.setNickname(account.getNickname());

        Role adminRole = roleRepo.findByName("ROLE_ADMIN").get();

        signinResponse.setIsAdmin(account.getRoles().contains(adminRole));
        return ResponseEntity.ok(signinResponse);
    }

    @PatchMapping("/user")
    public ResponseEntity<SigninResponse> updateUser(@RequestBody Map<String, Object> userRequestDto, @AuthenticationPrincipal UserDetails userDetails) {
        System.out.println("update user info");
        Account account = userDetailsService.findByEmail(userDetails.getUsername());

        return ResponseEntity.ok(authorizationService.updateUser(userRequestDto, account));
    }

    @PatchMapping("/updatePassword")
    public ResponseEntity<String> updatePassword(@RequestBody PasswordRequestDto passwordRequestDto,
                                                 @AuthenticationPrincipal UserDetails userDetails) {
        System.out.println("update user password");
        Account account = userDetailsService.findByEmail(userDetails.getUsername());
        try {
            authorizationService.updatePassword(passwordRequestDto, account);
            return new ResponseEntity<>(HttpStatus.OK);

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }

    }
}

