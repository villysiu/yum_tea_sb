package com.villysiu.yumtea.service;

import com.villysiu.yumtea.dto.request.SignupRequest;
import com.villysiu.yumtea.dto.request.SigninRequest;
import com.villysiu.yumtea.validation.EmailExistsException;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {
    ResponseEntity<String> signup(SignupRequest request) throws EmailExistsException;

    ResponseEntity<String> signin(SigninRequest request);
}