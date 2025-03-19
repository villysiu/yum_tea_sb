package com.villysiu.yumtea.service.impl;

import com.villysiu.yumtea.models.tea.Category;
import com.villysiu.yumtea.repo.tea.CategoryRepo;
import com.villysiu.yumtea.service.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service

public class CategoryServiceImpl implements CategoryService {


    private final CategoryRepo categoryRepo;
    @Autowired
    public CategoryServiceImpl(CategoryRepo categoryRepo) {
        this.categoryRepo = categoryRepo;
    }


    @Override
    public List<Category> getCategories() {
        return categoryRepo.findAll();
    }

    @Override
    public Category getCategoryById(Long id){
        return categoryRepo.findById(id).orElseThrow(()->new EntityNotFoundException("Category not found."));
    }

    @Override
    public Category updateCategory(Long id, Map<String, Object> categoryDto){

        Category category = categoryRepo.findById(id)
                .orElseThrow(()->new EntityNotFoundException("Category not found."));

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
    public Category createCategory(Category category) {
        return categoryRepo.save(category);
    }

    @Override
    public String deleteCategory(Long id) {
        if(!categoryRepo.existsById(id)) {
            throw new EntityNotFoundException("Category not found.");
        }
        categoryRepo.deleteById(id);
        return "Category deleted";
    }

}

