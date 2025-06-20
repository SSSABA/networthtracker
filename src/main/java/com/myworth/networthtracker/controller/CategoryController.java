package com.myworth.networthtracker.controller;

import com.myworth.networthtracker.enums.TransactionType;
import com.myworth.networthtracker.model.Category;
import com.myworth.networthtracker.repository.AccountRepository;
import com.myworth.networthtracker.repository.CategoryRepository;
import com.myworth.networthtracker.repository.TransactionRepository;
import com.myworth.networthtracker.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryRepository categoryRepository; // Keep for write operations
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final CategoryService categoryService; // <-- INJECT THE NEW SERVICE

    public CategoryController(CategoryRepository categoryRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, CategoryService categoryService) {
        this.categoryRepository = categoryRepository;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.categoryService = categoryService;
    }

    @GetMapping
    public String listCategories(Model model) {
        // USE THE SERVICE to get fully loaded data
        List<Category> parentCategories = categoryService.getParentCategoriesWithSubcategories();
        model.addAttribute("parentCategories", parentCategories);

        // Data for the "Add Category" form
        if (!model.containsAttribute("newCategory")) {
            model.addAttribute("newCategory", new Category());
        }
        model.addAttribute("allAccounts", accountRepository.findAll());
        model.addAttribute("transactionTypes", TransactionType.values());

        return "categories";
    }

    @PostMapping("/add")
    public String addCategory(@ModelAttribute("newCategory") Category newCategory,
                              @RequestParam(value = "parentId", required = false) Long parentId) {
        if (parentId != null) {
            Category parent = categoryRepository.findById(parentId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid parent category Id:" + parentId));
            // A sub-category inherits its type and account from its parent
            newCategory.setParentCategory(parent);
            newCategory.setTransactionType(parent.getTransactionType());
            newCategory.setDefaultAccount(parent.getDefaultAccount());
        }
        categoryRepository.save(newCategory);
        return "redirect:/categories";
    }

    @PostMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        long count = transactionRepository.countByCategory_Id(id);
        if (count > 0) {
            redirectAttributes.addFlashAttribute("errorMessage", "Cannot delete category: it is used by " + count + " transaction(s).");
            return "redirect:/categories";
        }

        Category categoryToDelete = categoryRepository.findById(id).orElse(null);
        if (categoryToDelete != null && !categoryToDelete.getSubCategories().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Cannot delete category: it has sub-categories. Please delete them first.");
            return "redirect:/categories";
        }

        categoryRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Category deleted successfully.");
        return "redirect:/categories";
    }
}
