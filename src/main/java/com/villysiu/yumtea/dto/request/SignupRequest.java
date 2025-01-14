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
    private String userName;
//    private String lastName;
    private String email;
    private String password;


}
