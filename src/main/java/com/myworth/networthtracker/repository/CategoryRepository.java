package com.myworth.networthtracker.repository;

import com.myworth.networthtracker.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    // This is the only query we need now for fetching parent categories for our views.
    @Query("SELECT DISTINCT p FROM Category p LEFT JOIN FETCH p.subCategories WHERE p.parentCategory IS NULL")
    List<Category> findAllParentCategoriesWithSubCategories();

    // The findByParentCategoryIsNull() method is no longer needed and can be removed.
}