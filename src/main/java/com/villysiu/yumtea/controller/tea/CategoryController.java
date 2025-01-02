package com.villysiu.yumtea.controller.tea;

import com.villysiu.yumtea.models.tea.Category;
import com.villysiu.yumtea.repo.tea.CategoryRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CategoryController {

//    @Autowired
    private final CategoryRepo categoryRepo;

    @GetMapping("/categories")
    public List<Category> getCategories() {
        return categoryRepo.findAll();
    }

    @PostMapping(value="/categories/new")
    public String addCategory(@RequestBody Category category) {
        categoryRepo.save(category);
        return category.getTitle() + " added";
    }

    @PutMapping("/categories/update/{id}")
    public String editCategory(@PathVariable Long id, @RequestBody Category category) {
        Category updateCategory = categoryRepo.findById(id).get();
        updateCategory.setTitle(category.getTitle());
        updateCategory.setDescription(category.getDescription());
        updateCategory.setImage_path(category.getImage_path());
        categoryRepo.save(updateCategory);
        return updateCategory.getTitle() + " updated";
    }

    @DeleteMapping("/categories/delete/{id}")
    public String deleteCategory(@PathVariable Long id) {
        Category deleteCategory = categoryRepo.findById(id).get();
        categoryRepo.delete(deleteCategory);
        return "Category deleted";
    }
}
