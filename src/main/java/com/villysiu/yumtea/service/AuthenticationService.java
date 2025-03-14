package com.villysiu.yumtea.service;

import com.villysiu.yumtea.dto.request.SignupRequest;
import com.villysiu.yumtea.dto.request.SigninRequest;
import com.villysiu.yumtea.dto.response.SigninResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;


public interface AuthenticationService {
    Long signup(SignupRequest signupRequest);
    SigninResponse signin(SigninRequest signinRequest, HttpServletRequest request);
}