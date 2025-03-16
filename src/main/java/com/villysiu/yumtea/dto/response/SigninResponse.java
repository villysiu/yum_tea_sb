package com.villysiu.yumtea.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class SigninResponse {
    Long id;
    String nickname;
    String email;
    Boolean isAdmin = false;

}
