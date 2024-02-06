package com.oasis.tasker.controllers;


import com.oasis.tasker.converters.category.CategoryToCategoryCommand;
import com.oasis.tasker.dtos.CategoryCommand;
import com.oasis.tasker.entities.Category;
import com.oasis.tasker.repositories.CategoryRepository;
import com.oasis.tasker.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {
    private final AuthService authService;
    private final CategoryToCategoryCommand categoryToCategoryCommand;
    private final CategoryRepository categoryRepository;


    public CategoryController(AuthService authService,
                              CategoryRepository categoryRepository,
                              CategoryToCategoryCommand categoryToCategoryCommand) {
        this.authService = authService;
        this.categoryToCategoryCommand = categoryToCategoryCommand;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping()
    public ResponseEntity<List<CategoryCommand>> myCategories() {
        System.out.println("We got here like joke");
        List<CategoryCommand> categoryCommand = new ArrayList<>();
        categoryRepository.findByUserId(authService.getLoggedInUser().getId()).forEach(
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
                newCategory(categoryCommand)
        );
    }

    @DeleteMapping("/{id}")
    void deleteCategory(@PathVariable Long id) {
        validateCategoryOwnership(id);
        categoryRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    CategoryCommand replaceCategory (@RequestBody CategoryCommand categoryCommand, @PathVariable Long id) {
        validateCategoryOwnership(id);
        return categoryRepository.findById(id)
                .map(category -> {
                    category.setName(categoryCommand.getName());

                    return categoryToCategoryCommand.convert(category);
                })
                .orElseGet(() -> newCategory(categoryCommand));

    }


    private CategoryCommand newCategory(CategoryCommand categoryCommand) {
        return categoryToCategoryCommand.convert(
                categoryRepository.save(
                        new Category(
                                categoryCommand.getName(),
                                authService.getLoggedInUser()
                        )
                )
        );
    }

    private void validateCategoryOwnership(Long taskId) throws ResponseStatusException {
        Optional<Category> catgoryOptional = categoryRepository.findById(taskId);
        if (catgoryOptional.get().getUser().getId() != authService.getLoggedInUser().getId())
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to update this task");
    }
}
