package com.villysiu.yumtea.service;

import com.villysiu.yumtea.dto.response.UserResponseDto;
import com.villysiu.yumtea.models.user.User;

import java.util.Map;

public interface AuthorizationService {
    User updateUser(Map<String, Object> userRequestDto, User user);
}
