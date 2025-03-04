package com.villysiu.yumtea.service.impl;

import com.villysiu.yumtea.models.user.Role;
import com.villysiu.yumtea.models.user.User;
import com.villysiu.yumtea.repo.user.RoleRepo;
import com.villysiu.yumtea.repo.user.UserRepo;
import lombok.NonNull;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private final UserRepo userRepo;
    @Autowired
    private final RoleRepo roleRepo;

    public CustomUserDetailsServiceImpl(UserRepo userRepo, RoleRepo roleRepo) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email + " not found."));

        Set<GrantedAuthority> authorities = user
                .getRoles()
                .stream()
                .map((role) -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toSet());

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }
    public User findByEmail(String email) {
        return userRepo.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email + " not found." ));
    }

    public Boolean isAdmin(@NonNull User user) {
        Role r = roleRepo.findByName("ROLE_ADMIN").orElse(createRole("ROLE_ADMIN"));
        return user.getRoles().contains(r);
    }
    private Role createRole(String roleName) {
        Role r = new Role(roleName);
        return roleRepo.save(r);


    }
}
