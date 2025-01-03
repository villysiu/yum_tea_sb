package com.villysiu.yumtea.service;

import com.villysiu.yumtea.models.tea.Category;

import java.util.Map;

public interface CategoryService {
    Category updateCategory(Long id, Map<String, Object> menuitemDto) throws RuntimeException;
}
