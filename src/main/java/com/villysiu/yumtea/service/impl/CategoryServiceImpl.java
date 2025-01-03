package com.villysiu.yumtea.service.impl;

import com.villysiu.yumtea.models.tea.Category;
import com.villysiu.yumtea.models.tea.Menuitem;
import com.villysiu.yumtea.repo.tea.CategoryRepo;
import com.villysiu.yumtea.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepo categoryRepo;

    @Override
    public Category updateCategory(Long id, Map<String, Object> categoryDto) throws RuntimeException {
        Category category = categoryRepo.findById(id)
                .orElseThrow(()->new RuntimeException("Category not found."));
        for(Map.Entry<String, Object> categoryDtoEntry : categoryDto.entrySet()) {
            String field = categoryDtoEntry.getKey();
            Object value = categoryDtoEntry.getValue();

            switch (field) {
                case "title":
                    category.setTitle((String)value);
                    break;
                case "description":
                    category.setDescription((String)value);
                    break;
                case "imageUrl":
                    category.setImageUrl((String)value);
                    break;
                default:
                    break;
            }
            categoryRepo.save(category);
        }
        return category;
    }
}

