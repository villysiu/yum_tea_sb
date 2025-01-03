package com.villysiu.yumtea.repo.tea;

import com.villysiu.yumtea.models.tea.Menuitem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuitemRepo extends JpaRepository<Menuitem, Long> {
    List<Menuitem> findByCategoryId(Long categoryId);
}
