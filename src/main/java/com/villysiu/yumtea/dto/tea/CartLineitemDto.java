package com.villysiu.yumtea.dto.tea;

import com.villysiu.yumtea.models.tea.Sugar;
import com.villysiu.yumtea.models.tea.Temperature;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartLineitemDto {
    private Long userID;
    private Long menuitemId;
    private Long milkId;
    private Double price;
    private Temperature temperature;
    private Sugar sugar;
    private Long sizeId;
    private Integer quantity;

}
