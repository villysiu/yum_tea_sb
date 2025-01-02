package com.villysiu.yumtea.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {

    String generateToken(UserDetails userDetails);
    Boolean isTokenValid(String token, UserDetails userDetails);
    String extractUsername(String token);
}
