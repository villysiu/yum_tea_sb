package com.villysiu.yumtea.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class SigninResponse {
    Long id;
    String nickname;
    String email;
    String token;
    Boolean isAdmin = false;

}
