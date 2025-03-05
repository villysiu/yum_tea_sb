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




    <T> List<T> findByAccountIdOrderByIdDesc(Long accountId, Class<T> type);

    <T> Optional<T> findByIdAndAccountId(Long id, Long accountId, Class<T> type);

    <T> Optional<T> findById(Long id, Class<T> type);

    void deleteAllByAccountId(Long accountId);
}
