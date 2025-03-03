package com.villysiu.yumtea.service.impl;

import com.villysiu.yumtea.dto.response.UserResponseDto;
import com.villysiu.yumtea.models.user.User;
import com.villysiu.yumtea.repo.user.UserRepo;
import com.villysiu.yumtea.service.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthorizationServiceImpl implements AuthorizationService {
    @Autowired
    private UserRepo userRepo;

    @Override
    public User updateUser(Map<String, Object> userRequestDto, User user) {
        for(Map.Entry<String, Object> entry : userRequestDto.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if(key.equals("nickname")) {
                user.setNickname((String) value);

            }
            if(key.equals("password")) {
                user.setPassword((String) value);
            }

        }
        userRepo.save(user);
//        UserResponseDto userResponseDto = new UserResponseDto();
//        userResponseDto.setNickname(user.getNickname());
        return user;
    }
}
