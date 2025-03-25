package com.villysiu.yumtea.repo.purchase;

import com.villysiu.yumtea.dto.response.BestSellerDto;

import com.villysiu.yumtea.models.purchase.PurchaseLineitem;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PurchaseLineitemRepo extends JpaRepository<PurchaseLineitem, Long> {

    @Query("SELECT pl.menuitem.id, m.title, sum(pl.quantity) " +
            "FROM PurchaseLineitem pl " +
            "right join Menuitem m on m.id = pl.menuitem.id " +
            "group by m.id " +
            "order by 3 desc"
    )
    List<Object[]> findAllSalesByMenuitem();

    @Query("SELECT pl.milk.id, mk.title, sum(pl.quantity) " +
            "FROM PurchaseLineitem pl " +
            "right join Milk mk on mk.id = pl.milk.id " +
            "group by mk.id " +
            "order by 3 desc"
    )
    List<Object[]> findPopularMilk();


//    @Query("SELECT pl.menuitem.id, m.title, sum(pl.quantity) " +
//            "FROM PurchaseLineitem pl " +
//            "RIGHT JOIN Menuitem m ON m.id = pl.menuitem.id " +
//            "WHERE pl.purchase.purchaseDate >= CURRENT_DATE - INTERVAL '30 days' " +
//            "GROUP BY m.id " +
//            "ORDER BY sum(pl.quantity) DESC")
//    List<Object[]> findMonthlyBestSeller(Pageable pageable);

    @Query("SELECT pl.menuitem.id, m.title, sum(pl.quantity) " +
            "FROM PurchaseLineitem pl " +
            "RIGHT JOIN Menuitem m ON m.id = pl.menuitem.id " +
            "WHERE pl.purchase.purchaseDate >= :startDate " +
            "GROUP BY m.id " +
            "ORDER BY sum(pl.quantity) DESC")
    List<Object[]> findMonthlyBestSellers(@Param("startDate") LocalDate startDate, Pageable pageable);
}
