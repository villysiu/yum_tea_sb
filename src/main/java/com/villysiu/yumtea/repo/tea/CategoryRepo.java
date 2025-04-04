package com.villysiu.yumtea.repo.tea;

import com.villysiu.yumtea.models.tea.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


public interface CategoryRepo extends JpaRepository<Category, Long> {
    Optional<Category> findByTitle(String title);
}
