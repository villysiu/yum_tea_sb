package com.villysiu.yumtea.controller.user;

import com.villysiu.yumtea.dto.response.SigninResponse;
import com.villysiu.yumtea.dto.response.UserResponseDto;
import com.villysiu.yumtea.models.user.User;
import com.villysiu.yumtea.service.AuthorizationService;
import com.villysiu.yumtea.service.impl.CustomUserDetailsServiceImpl;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/resource")
public class AuthorizationController {


    @Autowired
    private  CustomUserDetailsServiceImpl userDetailsService;

    private final SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
    @Autowired
    private AuthorizationService authorizationService;


    public AuthorizationController(CustomUserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
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
        authorizationService.updateUser(userRequestDto, user);

        SigninResponse signinResponse = new SigninResponse();
        signinResponse.setEmail(user.getEmail());
        signinResponse.setNickname(user.getNickname());

        return ResponseEntity.ok(signinResponse);
    }

//    @GetMapping("/invalidSession")
//    public ResponseEntity<String> sessionExpired(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
//        System.out.println("handling session Expire ");
//
//        logoutHandler.logout(request, response, authentication);
//
//        request.getSession().removeAttribute("SPRING_SECURITY_CONTEXT");
//        request.getSession().invalidate();
//        SecurityContextHolder.clearContext();
//        return new ResponseEntity<>("session expired", HttpStatus.UNAUTHORIZED);
////        return "sessionExpired";  // This is your session expired view (e.g., a page saying the session expired)
//    }


}
