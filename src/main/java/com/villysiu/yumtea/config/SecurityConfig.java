package com.villysiu.yumtea.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.time.Duration;
import java.util.Arrays;



@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

//    private final CorsConfigurationSource corsConfiguration = new CorsConfiguration();



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        System.out.println("security filter chain");

        http
            .csrf(AbstractHttpConfigurer::disable)
            .exceptionHandling(exceptionHandling -> exceptionHandling
                .authenticationEntryPoint(new RestAuthenticationEntryPoint())
            )
            .authorizeHttpRequests(auth -> auth

                .requestMatchers("/cart","/auth/**", "/categories", "/category/*/menuitems","/milks", "/menuitems", "/sizes", "/sugars", "/taxes/*").permitAll()
                .requestMatchers("/category/**", "/milk/**", "/menuitem/**", "size/**").hasAuthority("ROLE_ADMIN")
                .anyRequest().authenticated()
            );

        http
            .sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
//                            .maximumSessions(1)
//                    https://docs.spring.io/spring-security/reference/servlet/authentication/session-management.html#_detecting_timeouts
//                            .invalidSessionUrl("/invalidSession")


            );

        return http.build();
    }

//    @Controller
//public class SessionController {
//
//    @GetMapping("/session-expired")
//    public String sessionExpired() {
//        return "sessionExpired";  // This is your session expired view (e.g., a page saying the session expired)
//    }
//}

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager( UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(authenticationProvider);
    }

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://127.0.0.1:8001"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }



//    CorsConfiguration corsConfig = new CorsConfiguration();
//    corsConfig.setAllowedOrigins(Arrays.asList("http://127.0.0.1:8001")); // Frontend URL
//    corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")); // HTTP methods
//    corsConfig.setAllowedHeaders(Arrays.asList("*")); // All headers
//    corsConfig.setAllowCredentials(true); // Allow credentials (cookies)
//    return corsConfig;

}





