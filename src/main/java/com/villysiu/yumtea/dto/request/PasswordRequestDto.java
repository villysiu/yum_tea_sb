package com.villysiu.yumtea.dto.request;

import lombok.Data;

@Data
public class PasswordRequestDto {
    private String currentPassword;
    private String newPassword;
}
