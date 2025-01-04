package com.villysiu.yumtea.repo;

import com.villysiu.yumtea.models.CartLineitem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartLineitemRepo extends JpaRepository<CartLineitem, Long> {
    List<CartLineitem> findByUserId(Long id);
}
