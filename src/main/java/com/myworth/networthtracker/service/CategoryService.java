package com.myworth.networthtracker.service;

import com.myworth.networthtracker.model.Category;
import com.myworth.networthtracker.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * This method fetches all parent categories and ensures their sub-category collections
     * are fully loaded within an active transaction, preventing lazy loading issues in the view.
     * @return A list of parent categories with their children initialized.
     */
    @Transactional(readOnly = true) // This is the crucial annotation
    public List<Category> getParentCategoriesWithSubcategories() {
        return categoryRepository.findAllParentCategoriesWithSubCategories();
    }
}
