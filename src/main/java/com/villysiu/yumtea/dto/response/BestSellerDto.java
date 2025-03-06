package com.villysiu.yumtea.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BestSellerDto {
    private Long menuitemId;
    private String menuitemTitle;
    private Long count;
}
