package com.villysiu.yumtea.service;

import com.villysiu.yumtea.dao.request.SignupRequest;
import com.villysiu.yumtea.dao.response.JwtAuthenticationResponse;
import com.villysiu.yumtea.dao.request.SigninRequest;
import com.villysiu.yumtea.validation.EmailExistsException;

public interface AuthenticationService {
    JwtAuthenticationResponse signup(SignupRequest request) throws EmailExistsException;

    JwtAuthenticationResponse signin(SigninRequest request);
}