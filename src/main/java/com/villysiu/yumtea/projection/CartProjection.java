package com.villysiu.yumtea.projection;

public interface CartProjection {

    Long getMenuitemId();
    String getMenuitemTitle();

    Long getSizeId();
    String getSizeTitle();

    Long getMilkId();
    String getMilkTitle();

    String getTemperature();
//
    String getSugar();

    Double getPrice();
    Integer getQuantity();

}
