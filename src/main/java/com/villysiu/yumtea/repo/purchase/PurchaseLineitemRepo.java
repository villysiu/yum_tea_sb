package com.villysiu.yumtea.repo.purchase;

import com.villysiu.yumtea.dto.response.BestSellerDto;

import com.villysiu.yumtea.models.purchase.PurchaseLineitem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PurchaseLineitemRepo extends JpaRepository<PurchaseLineitem, Long> {

    @Query("SELECT pl.menuitem.id, m.title, sum(pl.quantity) " +
            "FROM PurchaseLineitem pl " +
            "right join Menuitem m on m.id = pl.menuitem.id " +
            "group by m.id " +
            "order by 3 desc"
    )
    List<Object[]> findSoldCountByMenuitem();

    @Query("SELECT pl.milk.id, mk.title, sum(pl.quantity) " +
            "FROM PurchaseLineitem pl " +
            "right join Milk mk on mk.id = pl.milk.id " +
            "group by mk.id " +
            "order by 3 desc"
    )
    List<Object[]> findPopularMilk();
}
