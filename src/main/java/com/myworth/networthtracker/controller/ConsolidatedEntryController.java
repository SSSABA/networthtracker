package com.myworth.networthtracker.controller;

import com.myworth.networthtracker.dto.ConsolidatedFormWrapper;
import com.myworth.networthtracker.dto.DailyEntryDTO;
import com.myworth.networthtracker.model.Category;
import com.myworth.networthtracker.model.Transaction;
import com.myworth.networthtracker.repository.CategoryRepository;
import com.myworth.networthtracker.repository.TransactionRepository;
import com.myworth.networthtracker.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class ConsolidatedEntryController {

    private final CategoryRepository categoryRepository; // Keep for caching
    private final TransactionRepository transactionRepository;
    private final CategoryService categoryService; // <-- INJECT THE NEW SERVICE

    public ConsolidatedEntryController(CategoryRepository categoryRepository, TransactionRepository transactionRepository, CategoryService categoryService) {
        this.categoryRepository = categoryRepository;
        this.transactionRepository = transactionRepository;
        this.categoryService = categoryService;
    }

    @GetMapping("/consolidated-entry")
    public String showConsolidatedForm(Model model) {
        // USE THE SERVICE to get fully loaded data
        List<Category> parentCategories = categoryService.getParentCategoriesWithSubcategories();

        ConsolidatedFormWrapper formWrapper = new ConsolidatedFormWrapper();
        formWrapper.getDailyEntries().add(new DailyEntryDTO());

        model.addAttribute("formWrapper", formWrapper);
        model.addAttribute("parentCategories", parentCategories);
        return "consolidated-entry";
    }

    @PostMapping("/consolidated-entry")
    public String saveConsolidatedForm(@ModelAttribute ConsolidatedFormWrapper formWrapper) {
        List<Transaction> transactionsToSave = new ArrayList<>();
        // Pre-caching all categories is still a good idea for the form processing part.
        Map<Long, Category> categoryCache = categoryRepository.findAll().stream()
                .collect(Collectors.toMap(Category::getId, c -> c));

        for (DailyEntryDTO dailyEntry : formWrapper.getDailyEntries()) {
            if (dailyEntry.getDate() == null) continue;

            for (Map.Entry<Long, BigDecimal> amountEntry : dailyEntry.getAmounts().entrySet()) {
                if (amountEntry.getValue() == null || amountEntry.getValue().compareTo(BigDecimal.ZERO) == 0) {
                    continue;
                }

                Long subCategoryId = amountEntry.getKey();
                BigDecimal amount = amountEntry.getValue();
                Category subCategory = categoryCache.get(subCategoryId);

                if (subCategory != null) {
                    Transaction transaction = new Transaction();
                    transaction.setDate(dailyEntry.getDate());
                    transaction.setAmount(amount);
                    transaction.setCategory(subCategory);
                    transaction.setDescription(subCategory.getName());
                    transaction.setType(subCategory.getTransactionType());
                    transaction.setAccount(subCategory.getDefaultAccount());
                    transactionsToSave.add(transaction);
                }
            }
        }

        if (!transactionsToSave.isEmpty()) {
            transactionRepository.saveAll(transactionsToSave);
        }

        return "redirect:/transactions";
    }
}
