package com.villysiu.yumtea.projection;

import lombok.Data;


public interface CartProjection {

    Long getId();
    PurchaseProjection.MenuitemProjection getMenuitem();
//    Long getMenuitemId();
//    String getMenuitemTitle();

    PurchaseProjection.SizeProjection getSize();
//    Long getSizeId();
//    String getSizeTitle();

    PurchaseProjection.MilkProjection getMilk();
//    Long getMilkId();
//    String getMilkTitle();

    String getTemperature();

    String getSugar();

    Double getPrice();
    Integer getQuantity();


}

