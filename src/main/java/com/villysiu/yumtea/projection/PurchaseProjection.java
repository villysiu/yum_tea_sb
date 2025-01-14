package com.villysiu.yumtea.projection;

import com.villysiu.yumtea.models.purchase.PurchaseLineitem;
import com.villysiu.yumtea.models.tea.Menuitem;
import com.villysiu.yumtea.models.tea.Sugar;
import com.villysiu.yumtea.models.tea.Temperature;
import com.villysiu.yumtea.models.user.User;

import java.util.Date;
import java.util.List;

public interface PurchaseProjection {
    Long getId();
    UserProjection getUser();

    Date getPurchaseDate();
    Double getTip();

    List<PurchaseLineitemProjection> getPurchaseLineitemList();

    interface PurchaseLineitemProjection {
        String getId();
        MenuitemProjection getMenuitem();


        MilkProjection getMilk();
        SizeProjection getSize();

        Temperature getTemperature();
        Sugar getSugar();

        int getQuantity();
        Double getPrice();
    }
    interface UserProjection {
        Long getId();
        String getUsername();
    }
    interface MenuitemProjection {
        Long getId();
        String getTitle();

    }
    interface MilkProjection {
        Long getId();
        String getTitle();
    }
    interface SizeProjection {
        Long getId();
        String getTitle();
    }
}
