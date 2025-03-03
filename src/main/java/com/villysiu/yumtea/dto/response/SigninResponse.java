package com.villysiu.yumtea.dto.response;

import com.villysiu.yumtea.models.user.User;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class SigninResponse {
//    Long id;
    String nickname;
    String email;

}
