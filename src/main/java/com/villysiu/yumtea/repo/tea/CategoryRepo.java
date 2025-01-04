package com.villysiu.yumtea.repo.tea;

import com.villysiu.yumtea.models.tea.Category;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CategoryRepo extends JpaRepository<Category, Long> { }
