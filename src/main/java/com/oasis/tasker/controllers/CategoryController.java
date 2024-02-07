package com.oasis.tasker.controllers;


import com.oasis.tasker.converters.category.CategoryToCategoryCommand;
import com.oasis.tasker.dtos.CategoryCommand;
import com.oasis.tasker.entities.Category;
import com.oasis.tasker.repositories.CategoryRepository;
import com.oasis.tasker.services.AuthService;
import com.oasis.tasker.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {

    @Autowired
    private AuthService authService;

    private final CategoryService categoryService;
    private final CategoryToCategoryCommand categoryToCategoryCommand;


    public CategoryController(CategoryService categoryService,
                              CategoryToCategoryCommand categoryToCategoryCommand) {
        this.categoryToCategoryCommand = categoryToCategoryCommand;
        this.categoryService = categoryService;
    }

    @GetMapping()
    public ResponseEntity<List<CategoryCommand>> myCategories() {
        List<CategoryCommand> categoryCommand = new ArrayList<>();
        categoryService.getUserCategories(authService.getLoggedInUser().getId()).forEach(
                category -> {
                    categoryCommand.add(this.categoryToCategoryCommand.convert(category));
                }
        );
        return ResponseEntity.ok(categoryCommand);
    }

    @PostMapping
    public ResponseEntity<CategoryCommand> create(@RequestBody CategoryCommand categoryCommand) {
        categoryCommand.setOwnerId(authService.getLoggedInUser().getId());
        return ResponseEntity.ok(
                categoryToCategoryCommand.convert(
                        categoryService.saveCategory(
                                categoryCommand,authService.getLoggedInUser()
                        )
                )
        );
    }

    @DeleteMapping("/{id}")
    void deleteCategory(@PathVariable Long id) {
        categoryService.validateCategoryOwnership(id, authService.getLoggedInUser().getId());
        categoryService.deleteCategoryById(id);
    }

    @PutMapping("/{id}")
    CategoryCommand replaceCategory (@RequestBody CategoryCommand categoryCommand, @PathVariable Long id) {
        categoryService.validateCategoryOwnership(id,authService.getLoggedInUser().getId());
        categoryCommand.setId(id);
        return categoryToCategoryCommand.convert(
                categoryService.updateCategory(categoryCommand, authService.getLoggedInUser())
        );

    }
}
