package com.villysiu.yumtea.service.impl;

import com.villysiu.yumtea.service.JwtService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import io.jsonwebtoken.Claims;
import javax.crypto.SecretKey;

@Service
public class JwtServiceImpl implements JwtService {

// set in .env
    @Value("${jwt.token.secret}")
    private String secret;


    @Override
    public String generateToken(String username){
//    public String generateToken(UserDetails userDetails) {
        /*
            generate token with jwts builder
            subject accepts string
            issued at and expireAt accept a date time object
            signWith accepts a secretKey
         */
        return Jwts.builder()
                .subject(username) //username here is indeed the email
//                .subject(userDetails.getUsername()) // returns email, overriddn in User
//                .claim("nickname", "silly goose) // add more info throught claim
//                .claim("role", "user")
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60* 60 * 24))
                .signWith(getSignInKey())
                .compact();
    }


    @Override
    public Boolean isTokenValid(String token, UserDetails userDetails) {
        Claims claims = extractAllClaims(token);
        String userName = claims.getSubject(); //email overridden in User model
        return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    @Override
    public String extractUsername(String token) {
        Claims claims = extractAllClaims(token);
        return claims.getSubject();
    }


    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(this.secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Boolean isTokenExpired(String token) {
        Claims claims = extractAllClaims(token);
        return claims.getExpiration().before(new Date());
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
