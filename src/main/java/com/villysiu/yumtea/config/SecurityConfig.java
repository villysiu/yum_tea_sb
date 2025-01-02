package com.villysiu.yumtea.config;

import com.villysiu.yumtea.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

//https://www.geeksforgeeks.org/spring-security-role-based-authentication/
//https://stackoverflow.com/questions/76723051/how-to-formlogin-since-websecurityconfigureradapter-is-deprecated
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserService userService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
//            .cors(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
//                .requestMatchers(HttpMethod.GET,"/categories", "/milks", "/menuitems").permitAll()
//                .requestMatchers("categories/**", "milk/**", "menuitem/**").hasRole("ADMIN")
                .requestMatchers("/api/v1/auth/**").permitAll()


                            .anyRequest().authenticated()
//                    auth.anyRequest().authenticated();
//                        auth.requestMatchers("/user").hasRole("USER");
//                        auth.requestMatchers("/admin").hasRole("Admin");
            )


            .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);


//            .httpBasic(Customizer.withDefaults());
//
//                .formLogin(Customizer.withDefaults());
//

        return http.build();
    }

//    @Bean
//    public UserDetailsService userDetailsService() {
//        UserDetails user = User.builder()
//                .username("user")
//                .password(passwordEncoder().encode("user123"))
//                .roles("USER")
//                .build();
//        UserDetails admin = User.builder()
//                .username("admin")
//                .password(passwordEncoder().encode("admin123"))
//                .roles("ADMIN")
//                .build();
//        return new InMemoryUserDetailsManager(user,admin);
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService.userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
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