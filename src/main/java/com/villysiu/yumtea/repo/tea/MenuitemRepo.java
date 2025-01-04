package com.villysiu.yumtea.repo.tea;

import com.villysiu.yumtea.models.tea.Menuitem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuitemRepo extends JpaRepository<Menuitem, Long> {
    List<Menuitem> findByCategoryId(Long categoryId);
}
