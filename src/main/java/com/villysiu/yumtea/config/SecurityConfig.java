package com.villysiu.yumtea.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sql.DataSource;
import java.io.IOException;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

//    private final UserService userService;

//    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        System.out.println("security filter chain");
        http
            .csrf(AbstractHttpConfigurer::disable)
//            .csrf(Customizer.withDefaults())
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling
                                .authenticationEntryPoint(new RestAuthenticationEntryPoint())
                )
            .authorizeHttpRequests(auth -> auth

                .requestMatchers("/cart","/api/v1/auth/**", "/categories", "/category/*/menuitems","/milks", "/menuitems").permitAll()
                .requestMatchers("/category/**", "/milk/**", "/menuitem/**").hasAuthority("ROLE_ADMIN")
                .anyRequest().authenticated()
            )
            .logout();

        http.sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
        );




        return http.build();
    }


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


}

// in case needed
//        @Bean
//        public CorsFilter corsFilter() {
//            UrlBasedCorsConfigurationSource source =
//                    new UrlBasedCorsConfigurationSource();
//            CorsConfiguration config = new CorsConfiguration();
//            config.setAllowCredentials(true);
//            config.addAllowedOrigin("*");
//            config.addAllowedHeader("*");
//            config.addAllowedMethod("*");
//            source.registerCorsConfiguration("/**", config);
//            return new CorsFilter(source);
//        }



//https://www.codejava.net/frameworks/spring-boot/spring-security-fix-deprecated-methods#google_vignette
//@Bean
//public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//    http.authorizeHttpRequests().requestMatchers("/signin", "/signup").permitAll()
//            .requestMatchers("/users/**", "/apps/**").hasAuthority("ADMIN")
//            .requestMatchers("/myapps/**").hasAuthority("CLIENT")
//            .anyRequest().authenticated()
//            .and().formLogin()
//            .loginPage("/signin")
//            .usernameParameter("email")
//            .defaultSuccessUrl("/", true)
//            .permitAll()
//            .and()
//            .rememberMe().key("AbcdEfghIjklmNopQrsTuvXyz_0123456789")
//            .and()
//            .logout().logoutUrl("/signout").permitAll();
//
//
//
//    return http.build();
//}