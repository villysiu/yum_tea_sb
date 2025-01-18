package com.villysiu.yumtea.service;

import com.villysiu.yumtea.dto.request.SignupRequest;
import com.villysiu.yumtea.dto.response.JwtAuthenticationResponse;
import com.villysiu.yumtea.dto.request.SigninRequest;
import com.villysiu.yumtea.validation.EmailExistsException;

public interface AuthenticationService {
    JwtAuthenticationResponse signup(SignupRequest request) throws EmailExistsException;

    JwtAuthenticationResponse signin(SigninRequest request);
}