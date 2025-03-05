package com.villysiu.yumtea.repo.cart;

import com.villysiu.yumtea.dto.request.CartInputDto;
import com.villysiu.yumtea.dto.response.CartResponseDto;
import com.villysiu.yumtea.models.cart.Cart;
import com.villysiu.yumtea.models.tea.Sugar;
import com.villysiu.yumtea.models.tea.Temperature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface CartRepo extends JpaRepository<Cart, Long> {



    Optional<Cart> findByAccountIdAndMenuitemIdAndMilkIdAndSizeIdAndSugarAndTemperature(

            Long account_id,
            Long menuitem_id,
            Long milk_id,
            Long size_id,
            Sugar sugar,
            Temperature temperature
    );


//    @Query("SELECT c.id, c.price, c.sugar, c.temperature, c.quantity, " +
//            "u.id as userId," +
//            "m.id as menuitemId, m.title as menuitemTitle," +
//            "mk.id as milkId, mk.title as milkTitle," +
//            "sz.id as sizeId, sz.title as sizeTitle from Cart c " +
//            "inner join User u on u.id = c.user.id " +
//            "inner join Menuitem m on m.id = c.menuitem.id " +
//            "inner join Milk mk on mk.id = c.milk.id " +
//            "inner join Size sz on sz.id = c.size.id " +
//            "where c.user.id = :userId")
//    List<Object>  findByUserIdQuery(Long userId);

    <T> List<T> findByAccountIdOrderByIdDesc(Long accountId, Class<T> type);



    <T> Optional<T> findById(Long id, Class<T> type);

    void deleteAllByAccountId(Long accountId);
}
