package com.villysiu.yumtea.config;

import com.villysiu.yumtea.models.user.User;
import com.villysiu.yumtea.service.JwtService;
import com.villysiu.yumtea.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


/*
* class that is executed first and ONLY ONE TIME  when request come in
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;

    /*
        this is the entry point when getting ANY request

        3 scenarios

        1. no header
            ie  No authorization header, or not started with bearer

        2. there is a bearer header but failed validatetion
        (maybe expired or FAKE!?!?!?!?!?)

        3. there is a jwt and is validated
            we extract the username (ie email) (refer to jwtServiceImpl token is built with email, issue Date time and exp, and a key)
            validate the jwt token along with user email
            then we create an empty context, regardless current context has content or not
            save the current user into it.


     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        System.out.println("request: " + request.getRequestURI());

        try{
            String jwt = parseJwt(request);

            if(jwt != null){
                String userEmail = jwtService.extractUsername(jwt);
                UserDetails userDetails = userService.loadUserByUsername(userEmail);

                if(jwtService.isTokenValid(jwt, userDetails)){
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContext context = SecurityContextHolder.createEmptyContext();
                    context.setAuthentication(authToken);
                    SecurityContextHolder.setContext(context);
                }
            }
            filterChain.doFilter(request, response);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private String parseJwt(HttpServletRequest request){

        final String authHeader = request.getHeader("Authorization");
        System.out.println("authHeader: " + authHeader);
        if (authHeader != null && authHeader.startsWith("Bearer ")){
            return authHeader.substring(7);
        }
        return null;
    }
}
