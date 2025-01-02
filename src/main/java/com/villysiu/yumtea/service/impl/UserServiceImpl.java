package com.villysiu.yumtea.service.impl;

import com.villysiu.yumtea.models.user.User;
import com.villysiu.yumtea.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import com.villysiu.yumtea.repo.user.UserRepo;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor

public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;

    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) {

                User user = userRepo.findByEmail(username);
                if (user == null) {
                    throw new UsernameNotFoundException("User not found");
                }
                else
                    return user;

            }
        };
    }


}
