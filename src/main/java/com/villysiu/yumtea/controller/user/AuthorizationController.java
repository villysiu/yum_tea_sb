package com.villysiu.yumtea.controller.user;

import com.villysiu.yumtea.dto.request.PasswordRequestDto;
import com.villysiu.yumtea.dto.response.SigninResponse;

import com.villysiu.yumtea.models.user.User;
import com.villysiu.yumtea.service.AuthorizationService;
import com.villysiu.yumtea.service.impl.CustomUserDetailsServiceImpl;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/resource")
public class AuthorizationController {

    private final CustomUserDetailsServiceImpl userDetailsService;
    private final AuthorizationService authorizationService;

    public AuthorizationController(CustomUserDetailsServiceImpl userDetailsService, AuthorizationService authorizationService) {
        this.userDetailsService = userDetailsService;
        this.authorizationService = authorizationService;
    }


    @GetMapping("/user")
    public ResponseEntity<SigninResponse> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userDetailsService.findByEmail(userDetails.getUsername());

        SigninResponse signinResponse = new SigninResponse();
        signinResponse.setEmail(user.getEmail());
        signinResponse.setNickname(user.getNickname());

        return ResponseEntity.ok(signinResponse);
    }

    @PatchMapping("/user")
    public ResponseEntity<SigninResponse> updateUser(@RequestBody Map<String, Object> userRequestDto, @AuthenticationPrincipal UserDetails userDetails){
        System.out.println("update user info");
        User user = userDetailsService.findByEmail(userDetails.getUsername());

        return ResponseEntity.ok(authorizationService.updateUser(userRequestDto, user));
    }

    @PatchMapping("/updatePassword")
    public ResponseEntity<String> updatePassword(@RequestBody PasswordRequestDto passwordRequestDto,
                                                  @AuthenticationPrincipal UserDetails userDetails) {
        System.out.println("update user password");
        User user = userDetailsService.findByEmail(userDetails.getUsername());
       try{
            authorizationService.updatePassword(passwordRequestDto, user);
           return new ResponseEntity<>(HttpStatus.OK);

        }catch (AuthenticationException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }




}
