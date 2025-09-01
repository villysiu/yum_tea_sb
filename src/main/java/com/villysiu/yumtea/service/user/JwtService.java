package com.villysiu.yumtea.service.user;

import com.villysiu.yumtea.config.JwtAuthenticationFilter;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.token.secret}")
    private String secret;

    @Value("${jwt.token.expires}")
    private Long jwtExpiresMinutes;

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    public String generateToken(String email) {

        String jwt = Jwts.builder()
                .subject(email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtExpiresMinutes * 60 * 1000))
                .signWith(getSignInKey())
                .compact();

        return jwt;
    }

    public Claims validateToken(String token) throws JwtException {

        try {
            logger.info("Validating JWT token");

            Claims claims = Jwts.parser()
                    .verifyWith(getSignInKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            logger.info("JWT token validated");
            return claims;

        } catch (JwtException e) {

            // This catches any JWT-related issues: expired, malformed, signature invalid,
            // etc.
            throw new JwtException("JWT validation failed: " + e.getMessage(), e);

        } catch (Exception e) {
            // Optional: catch other unexpected exceptions
            throw new JwtException("Unexpected error during JWT validation", e);
        }

    }

    private SecretKey getSignInKey() {
        // SignatureAlgorithm.HS256, this.secret
        byte[] keyBytes = Decoders.BASE64.decode(this.secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
