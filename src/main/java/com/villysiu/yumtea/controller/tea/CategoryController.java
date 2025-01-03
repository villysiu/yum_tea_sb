package com.villysiu.yumtea.controller.tea;

import com.villysiu.yumtea.models.tea.Category;
import com.villysiu.yumtea.models.tea.Menuitem;
import com.villysiu.yumtea.repo.tea.CategoryRepo;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CategoryController {

    @Autowired
    private  CategoryRepo categoryRepo;

    @GetMapping("/categories")
    public List<Category> getCategories() {
        return categoryRepo.findAll();
    }
    @GetMapping("/category/{id}/menuitems")
    public List<Menuitem> getCategoryMenuItems(@PathVariable Long id) {
        Category category = categoryRepo.findById(id)
                .orElseThrow(()-> new RuntimeException("Category not found"));
        return category.getMenuitems();
    }

//    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value="/category")
    public String addCategory(@RequestBody Category category) {
        categoryRepo.save(category);
        return category.getTitle() + " added";
    }

    @PutMapping("/category/{id}")
    public String editCategory(@PathVariable Long id, @RequestBody Category category) {
        Category updateCategory = categoryRepo.findById(id).get();
        updateCategory.setTitle(category.getTitle());
        updateCategory.setDescription(category.getDescription());
        updateCategory.setImage_path(category.getImage_path());
        categoryRepo.save(updateCategory);
        return updateCategory.getTitle() + " updated";
    }

    @DeleteMapping("/category/{id}")
    public String deleteCategory(@PathVariable Long id) {
        Category deleteCategory = categoryRepo.findById(id).get();
        categoryRepo.delete(deleteCategory);
        return "Category deleted";
    }
}
