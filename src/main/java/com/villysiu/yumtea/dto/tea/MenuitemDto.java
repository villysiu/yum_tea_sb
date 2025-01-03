package com.villysiu.yumtea.dto.tea;

import com.villysiu.yumtea.models.tea.Sugar;
import com.villysiu.yumtea.models.tea.Temperature;
import lombok.*;


@Data //includes getter setter tostring etc
@AllArgsConstructor
@NoArgsConstructor

public class MenuitemDto
{
    private String title;
    private String imageUrl;
    private String description;
    private Long categoryId;
    private Long milkId;
    private Double price;
    private Temperature temperature;
    private Sugar sugar;

}
