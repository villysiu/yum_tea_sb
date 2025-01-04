package com.villysiu.yumtea.dto.output;

import com.villysiu.yumtea.models.tea.Sugar;
import com.villysiu.yumtea.models.tea.Temperature;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartOutputDto {
    Long id;
    Long menuitemId;
    String menuitemTitle;

    Long sizeId;
    String sizeTitle;

    Long milkId;
    String milkTitle;

    Temperature temperature;

    Sugar sugar;

    Double price;
    Integer quantity;
}
