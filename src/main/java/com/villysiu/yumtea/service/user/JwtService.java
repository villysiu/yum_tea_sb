package com.villysiu.yumtea.service.user;

import com.villysiu.yumtea.config.JwtAuthenticationFilter;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;

import javax.crypto.SecretKey;

import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.token.secret}")
    private String secret;

    @Value("${jwt.token.expires}")
    private Long jwtExpiresMinutes;

    private Claims claims;

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    public void generateToken(String email, HttpServletResponse response) {
        /*
         * generate token with jwts builder
         * subject accepts string
         * issued at and expireAt accept a date time object
         * signWith accepts a secretKey
         */
        String jwt = Jwts.builder()
                .subject(email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtExpiresMinutes * 60 * 1000))
                .signWith(getSignInKey())
                .compact();

        // Build cookie manually with SameSite=None and Secure
        String cookie = "JWT=" + jwt +
                "; HttpOnly" +
                "; Secure" + // ✅ Needed for SameSite=None to work
                "; SameSite=None" + // ✅ Allows cross-origin
                "; Path=/" +
                "; Max-Age=" + (24 * 60 * 60);

        response.addHeader("Set-Cookie", cookie);
    }

    public String getJwtFromCookie(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, "JWT");

        if (cookie == null || cookie.getValue().isEmpty()) {
            throw new JwtException("JWT token is empty");
        }

        return cookie.getValue();

    }

    public void validateToken(String token) throws JwtException {

        try {
            logger.info("Validating JWT token");

            claims = Jwts.parser()
                    .verifyWith(getSignInKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            // return claims;
            logger.info("JWT token validated");

        } catch (JwtException e) {

            // This catches any JWT-related issues: expired, malformed, signature invalid,
            // etc.
            throw new JwtException("JWT validation failed: " + e.getMessage(), e);

        } catch (Exception e) {
            // Optional: catch other unexpected exceptions
            throw new JwtException("Unexpected error during JWT validation", e);
        }

    }

    public void removeTokenFromCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("JWT", null);
        cookie.setPath("/");

        response.addCookie(cookie);
    }

    private SecretKey getSignInKey() {
        // SignatureAlgorithm.HS256, this.secret
        byte[] keyBytes = Decoders.BASE64.decode(this.secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractEmail() {
        return claims.getSubject();
    }
}
