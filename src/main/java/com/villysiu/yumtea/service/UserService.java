package com.villysiu.yumtea.service;

import com.villysiu.yumtea.models.user.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService{

    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
    User getCurrentUser();
}
