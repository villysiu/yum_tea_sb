package com.villysiu.yumtea.service;

import com.villysiu.yumtea.models.user.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService {
    UserDetailsService userDetailsService();
    User getCurrentUser();
}
