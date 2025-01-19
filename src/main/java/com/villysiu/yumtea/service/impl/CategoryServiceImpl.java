package com.villysiu.yumtea.service.impl;

import com.villysiu.yumtea.models.tea.Category;
import com.villysiu.yumtea.repo.tea.CategoryRepo;
import com.villysiu.yumtea.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
//@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepo categoryRepo;


    @Override
    public List<Category> getCategories() {
        return categoryRepo.findAll();
    }

    @Override
    public Category updateCategory(Long id, Map<String, Object> categoryDto) throws RuntimeException {
        Category category = categoryRepo.findById(id)
                .orElseThrow(()->new RuntimeException("Category not found."));

        for(Map.Entry<String, Object> categoryDtoEntry : categoryDto.entrySet()) {
            String key = categoryDtoEntry.getKey();
            Object value = categoryDtoEntry.getValue();

            switch (key) {
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

    @Override
    public Category createCategory(Category category) throws RuntimeException {
        return categoryRepo.save(category);
    }

    @Override
    public String deleteCategory(Long id) {
        Category category = categoryRepo.findById(id).orElseThrow(()-> new RuntimeException("Category not found"));
        categoryRepo.delete(category);
        return "Category deleted";
    }
}

