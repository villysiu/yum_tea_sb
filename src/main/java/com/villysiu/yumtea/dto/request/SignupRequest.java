package com.villysiu.yumtea.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
//@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {
    private String nickname;
    private String email;
    private String password;


}
