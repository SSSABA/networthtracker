package com.myworth.networthtracker.controller;

import com.myworth.networthtracker.model.Category;
import com.myworth.networthtracker.repository.CategoryRepository;
import com.myworth.networthtracker.repository.TransactionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class CategoryController {

    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;

    public CategoryController(CategoryRepository categoryRepository, TransactionRepository transactionRepository) {
        this.categoryRepository = categoryRepository;
        this.transactionRepository = transactionRepository;
    }

    // This method displays the main categories page
    @GetMapping("/categories")
    public String listCategories(Model model) {
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);
        // This is for the "Add New Category" form
        model.addAttribute("newCategory", new Category());
        return "categories"; // Renders categories.html
    }

    // This method handles the form submission for adding a new category
    @PostMapping("/categories/add")
    public String addCategory(@ModelAttribute Category newCategory) {
        categoryRepository.save(newCategory);
        return "redirect:/categories"; // Redirect to refresh the page and list
    }

    // This method handles the deletion of a category
    @PostMapping("/categories/delete/{id}")
    public String deleteCategory(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        // IMPORTANT: Check if the category is in use before deleting
        long count = transactionRepository.countByCategory_Id(id); // You will need to add this method
        if (count > 0) {
            redirectAttributes.addFlashAttribute("errorMessage", "Cannot delete category as it is currently used by " + count + " transaction(s).");
        } else {
            categoryRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Category deleted successfully.");
        }
        return "redirect:/categories";
    }
}
