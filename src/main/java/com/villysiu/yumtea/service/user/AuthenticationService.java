package com.villysiu.yumtea.service.user;

import com.villysiu.yumtea.dto.request.SignupRequest;
import com.villysiu.yumtea.dto.request.SigninRequest;
import com.villysiu.yumtea.dto.response.SigninResponse;
import jakarta.servlet.http.HttpServletRequest;


public interface AuthenticationService {
    Long signup(SignupRequest signupRequest);
    SigninResponse signin(SigninRequest signinRequest, HttpServletRequest request);
}