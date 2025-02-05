package com.villysiu.yumtea.repo.cart;

import com.villysiu.yumtea.dao.CartProjection;
import com.villysiu.yumtea.models.cart.Cart;
import com.villysiu.yumtea.models.tea.Sugar;
import com.villysiu.yumtea.models.tea.Temperature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepo extends JpaRepository<Cart, Long> {


    Optional<Cart> findByUserIdAndMenuitemIdAndMilkIdAndSizeIdAndSugarAndTemperature(

            Long user_id,
            Long menuitem_id,
            Long milk_id,
            Long size_id,
            Sugar sugar,
            Temperature temperature
    );

    @Query("SELECT c.id, m.id as menuitemId, m.title as menuitem, c.price, c.quantity, c.sugar, c.temperature, mk.id," +
            " mk.title as Milk, s.id as sizeId, s.title as size "+
            "FROM Cart c inner join Menuitem m on c.menuitem.id = m.id" +
            " join Milk mk on c.milk.id = mk.id" +
            " join Size s on c.size.id = s.id where c.user.id = :userId"
    )
    List<Object[]>findByUserIdQuery(Long userId);


    // returning either cartProjection or Cart
    <T> List<T> findByUserId(Long id, Class<T> type);


    void deleteByUserId(Long userId);
    //    OR
//    @Modifying
//    @Query ("delete from Cart c where c.user.id = ?1")
//    void deleteInBulkByUserId(Long userId);


    <T> Optional<T> findById(Long id, Class<T> type);


}
