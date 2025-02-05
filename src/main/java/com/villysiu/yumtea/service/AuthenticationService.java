package com.villysiu.yumtea.service;

import com.villysiu.yumtea.dto.request.SignupRequest;
import com.villysiu.yumtea.dto.request.SigninRequest;
import com.villysiu.yumtea.models.user.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {
    ResponseEntity<String> signup(SignupRequest signupRequest);

    User signin(SigninRequest signinRequest, HttpServletRequest request);
}