package com.villysiu.yumtea.service;

import com.villysiu.yumtea.models.tea.Category;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Map;

public interface CategoryService {
    List<Category> getCategories();
    Category getCategoryById(Long id);

    Category updateCategory(Long id, Map<String, Object> categoryDto);
    Category createCategory(Category category);
    String deleteCategory(Long id);

}
