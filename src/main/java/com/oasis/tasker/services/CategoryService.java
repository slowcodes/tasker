package com.oasis.tasker.services;


import com.oasis.tasker.dtos.CategoryCommand;
import com.oasis.tasker.entities.Category;
import com.oasis.tasker.entities.User;
import com.oasis.tasker.repositories.CategoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public void validateCategoryOwnership(Long taskId, Long userId) throws ResponseStatusException {
        Optional<Category> catgoryOptional = categoryRepository.findById(taskId);
        if (catgoryOptional.get().getUser().getId() != userId)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to update this task");
    }

    public void deleteCategoryById(Long id) {
        categoryRepository.deleteById(id);
    }

    public Category saveCategory(CategoryCommand categoryCommand, User loggedInUser) {
        return categoryRepository.save(
                new Category(
                        categoryCommand.getName(),
                        loggedInUser
                )
        );
    }

    public Category updateCategory(CategoryCommand categoryCommand, User loggedInUser) {
        return categoryRepository.findById(categoryCommand.getId())
                .map(category -> {
                    category.setName(categoryCommand.getName());

                    return category;
                })
                .orElseGet(() -> saveCategory(categoryCommand,loggedInUser));
    }

    public List<Category> getUserCategories(Long userId) {

        return categoryRepository.findByUserId(userId);
    }
}

