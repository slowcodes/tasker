package com.oasis.tasker.repositories;

import com.oasis.tasker.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface CategoryRepository extends JpaRepository<Category, Long> {
    public Category getCategoryById(Long id);

    List<Category> findByUserId(Long id);
}
