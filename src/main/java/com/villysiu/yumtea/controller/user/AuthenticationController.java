package com.villysiu.yumtea.controller.user;

import com.villysiu.yumtea.dto.request.SignupRequest;
import com.villysiu.yumtea.dto.request.SigninRequest;
import com.villysiu.yumtea.dto.response.SigninResponse;
import com.villysiu.yumtea.service.AuthenticationService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")

public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }


    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequest request){
        System.out.println("in signup");
        //SignupRequest{userName='spring', email='springuser@gg.com', password='password'}
        try{
            Long id = authenticationService.signup(request);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (EntityExistsException e){
            return new ResponseEntity<>( e.getMessage(), HttpStatus.CONFLICT);
        }

    }

    @PostMapping("/login")
    public ResponseEntity<?> signin(@RequestBody SigninRequest signinRequest, HttpServletRequest request) {
        System.out.println("in sign in controller");
        try{
            SigninResponse signinResponse = authenticationService.signin(signinRequest, request );
            return new ResponseEntity<>(signinResponse ,HttpStatus.OK);

        } catch (AuthenticationException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }

    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication){
        System.out.println("logging out");

        logoutHandler.logout(request, response, authentication);

        request.getSession().removeAttribute("SPRING_SECURITY_CONTEXT");
        request.getSession().invalidate();
        SecurityContextHolder.clearContext();
        return new ResponseEntity<>("Logged out successfully", HttpStatus.OK);

    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

}