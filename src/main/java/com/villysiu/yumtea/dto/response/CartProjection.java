package com.villysiu.yumtea.dto.response;


public interface CartProjection {

    Long getId();
    PurchaseProjection.MenuitemProjection getMenuitem();

    PurchaseProjection.SizeProjection getSize();

    PurchaseProjection.MilkProjection getMilk();

    String getTemperature();

    String getSugar();

    Double getPrice();
    Integer getQuantity();


}

