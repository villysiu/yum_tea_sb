package com.villysiu.yumtea.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class CustomRequestFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        System.out.println("custom request filter");

        if (request.getContentType() != null && request.getContentType().contains("application/json")) {
            // Read the request body
            byte[] body = request.getInputStream().readAllBytes();
            System.out.println(new String(body));
            request.getInputStream().close();
        }
        filterChain.doFilter(request, response);
    }
}
