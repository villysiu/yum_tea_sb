package com.villysiu.yumtea.controller.tea;

import com.villysiu.yumtea.models.tea.Category;
import com.villysiu.yumtea.models.tea.Menuitem;
import com.villysiu.yumtea.repo.tea.CategoryRepo;
import com.villysiu.yumtea.service.CategoryService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class CategoryController {

    private final CategoryRepo categoryRepo;
    private final CategoryService categoryService;

    @GetMapping("/categories")
    public List<Category> getCategories() {
        return categoryRepo.findAll();
    }


//    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value="/category")
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        categoryRepo.save(category);
        return new ResponseEntity<>(category, HttpStatus.CREATED);
    }

    @PatchMapping("/category/{id}")
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
