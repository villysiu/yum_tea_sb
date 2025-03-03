package com.villysiu.yumtea.controller.user;

import ch.qos.logback.core.encoder.JsonEscapeUtil;
import com.villysiu.yumtea.dto.request.SignupRequest;
import com.villysiu.yumtea.dto.request.SigninRequest;
import com.villysiu.yumtea.dto.response.SigninResponse;
import com.villysiu.yumtea.models.user.User;
import com.villysiu.yumtea.service.AuthenticationService;
import com.villysiu.yumtea.exception.EmailExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();


    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequest request) {
        System.out.println("in signup");
        //SignupRequest{userName='spring', email='springuser@gg.com', password='password'}
        try{
            Long id = authenticationService.signup(request);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (EmailExistsException e){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

    }

    @PostMapping("/login")
    public ResponseEntity<SigninResponse> signin(@RequestBody SigninRequest signinRequest, HttpServletRequest request) {
        System.out.println("in sign in controller");
        try{
            SigninResponse signinResponse = authenticationService.signin(signinRequest, request );
            return new ResponseEntity<>(signinResponse ,HttpStatus.OK);

        } catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
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
//
//    @ExceptionHandler(EmailExistsException.class)
//    @ResponseStatus(HttpStatus.CONFLICT)
//    public ResponseEntity<String> handleEmailExistsExceptionException(EmailExistsException e) {
// System.out.println("emal nort found");
//        return ResponseEntity.status(HttpStatus.CONFLICT).body("email not exist");
//    }
}