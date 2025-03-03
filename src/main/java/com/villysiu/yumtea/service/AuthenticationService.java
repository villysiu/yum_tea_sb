package com.villysiu.yumtea.service;

import com.villysiu.yumtea.dto.request.SignupRequest;
import com.villysiu.yumtea.dto.request.SigninRequest;
import com.villysiu.yumtea.dto.response.SigninResponse;
import com.villysiu.yumtea.models.user.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {
    Long signup(SignupRequest signupRequest);
    SigninResponse signin(SigninRequest signinRequest, HttpServletRequest request);
}