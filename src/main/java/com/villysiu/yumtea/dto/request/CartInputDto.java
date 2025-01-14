package com.villysiu.yumtea.dto.request;

import com.villysiu.yumtea.models.tea.Sugar;
import com.villysiu.yumtea.models.tea.Temperature;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartInputDto {
    private Long menuitemId;
    private Long milkId;
    private Temperature temperature;
    private Sugar sugar;
    private Long sizeId;
    private Integer quantity;

}
