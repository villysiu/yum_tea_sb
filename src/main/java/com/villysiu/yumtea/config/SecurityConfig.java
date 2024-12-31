package com.villysiu.yumtea.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

//https://www.geeksforgeeks.org/spring-security-role-based-authentication/
//https://stackoverflow.com/questions/76723051/how-to-formlogin-since-websecurityconfigureradapter-is-deprecated
@Configuration
//@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable())
                .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/categories").permitAll()
                    .requestMatchers("categories/**").hasRole("ADMIN").anyRequest().authenticated()
                        .requestMatchers("/privateUserPage").hasRole("USER").anyRequest().authenticated()
//                        auth.anyRequest().authenticated();
//                        auth.requestMatchers("/user").hasRole("USER");
//                        auth.requestMatchers("/admin").hasRole("Admin");
                )
                .httpBasic(Customizer.withDefaults());
//
//                .formLogin(Customizer.withDefaults());
//

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.builder()
                .username("user")
                .password(passwordEncoder().encode("user123"))
                .roles("USER")
                .build();
        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder().encode("admin123"))
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(user,admin);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}



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