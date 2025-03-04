package com.villysiu.yumtea.dto.response;

import com.villysiu.yumtea.models.tea.Sugar;
import com.villysiu.yumtea.models.tea.Temperature;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CartResponseDto {
    private Long id;
    private Long userId;
    private Double price;
    private Sugar sugar;
    private Temperature temperature;
    private int quantity;
    private Long menuitemId;
    private String menuitemTitle;
    private Long milkId;
    private String milkTitle;
    private Long sizeId;
    private String sizeTitle;

}
