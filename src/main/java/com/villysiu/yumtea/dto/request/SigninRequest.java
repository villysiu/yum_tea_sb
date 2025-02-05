package com.villysiu.yumtea.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder

public class SigninRequest {
    private String email;
    private String password;


}

