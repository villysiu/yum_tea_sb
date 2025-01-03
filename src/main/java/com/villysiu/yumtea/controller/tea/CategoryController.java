package com.villysiu.yumtea.controller.tea;

import com.villysiu.yumtea.models.tea.Category;
import com.villysiu.yumtea.models.tea.Menuitem;
import com.villysiu.yumtea.repo.tea.CategoryRepo;
import com.villysiu.yumtea.service.CategoryService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class CategoryController {

    @Autowired
    private  CategoryRepo categoryRepo;

    @Autowired
    private CategoryService categoryService;
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
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        categoryRepo.save(category);
        return new ResponseEntity<>(category, HttpStatus.CREATED);
    }

    @PutMapping("/category/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @RequestBody Map<String, Object> categoryDto) {
        Category category = categoryService.updateCategory(id, categoryDto);
        return ResponseEntity.ok(category);
    }

    @DeleteMapping("/category/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        Category category = categoryRepo.findById(id).orElseThrow(()-> new RuntimeException("Category not found"));
        categoryRepo.delete(category);
        return ResponseEntity.ok("Category deleted.");
    }
}
