package com.villysiu.yumtea.projection;

import lombok.Data;


public interface CartProjection {

    Long getId();
    Long getMenuitemId();
    String getMenuitemTitle();

    Long getSizeId();
    String getSizeTitle();

    Long getMilkId();
    String getMilkTitle();

    String getTemperature();

    String getSugar();

    Double getPrice();
    Integer getQuantity();

}
