package com.villysiu.yumtea.service.user;

import com.villysiu.yumtea.dto.request.PasswordRequestDto;
import com.villysiu.yumtea.dto.response.SigninResponse;

import com.villysiu.yumtea.models.user.Account;

import java.util.List;
import java.util.Map;

public interface AuthorizationService {
    SigninResponse updateUser(Map<String, Object> userRequestDto, Account account);
    void updatePassword(PasswordRequestDto passwordRequestDto, Account account);
    List<SigninResponse> getAllAccounts();
    SigninResponse toggleAdminRole(Long id);
    void deleteAccount(Long id, Account authenticatedAccount);
    Account findByEmail(String email);
}
