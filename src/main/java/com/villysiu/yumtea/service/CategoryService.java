package com.villysiu.yumtea.service;

import com.villysiu.yumtea.models.tea.Category;

import java.util.List;
import java.util.Map;

public interface CategoryService {
    List<Category> getCategories();
    Category updateCategory(Long id, Map<String, Object> menuitemDto) throws RuntimeException;
    Category createCategory(Category category) throws RuntimeException;
    String deleteCategory(Long id);
}
