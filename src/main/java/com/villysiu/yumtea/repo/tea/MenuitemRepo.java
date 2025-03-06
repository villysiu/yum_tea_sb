package com.villysiu.yumtea.repo.tea;

import com.villysiu.yumtea.models.tea.Menuitem;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface MenuitemRepo extends JpaRepository<Menuitem, Long> {

    @Query("SELECT m FROM Menuitem m Where m.category.id=:categoryId")
    List<Menuitem> findByCategoryIdQuery(Long categoryId);

    @Query("SELECT pl.menuitem.id, m.title, count(*) " +
            "FROM PurchaseLineitem pl " +
            "inner join Menuitem m on m.id=pl.menuitem.id " +
            "group by pl.menuitem.id " +
            "order by count(*) desc ")
    List<Object[]> findBestSellers(Pageable pageable);


}
