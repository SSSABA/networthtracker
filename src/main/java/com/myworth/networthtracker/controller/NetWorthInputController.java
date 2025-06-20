package com.myworth.networthtracker.controller;

import com.myworth.networthtracker.enums.AccountType;
import com.myworth.networthtracker.enums.TransactionType;
import com.myworth.networthtracker.model.Account;
import com.myworth.networthtracker.model.Category;
import com.myworth.networthtracker.model.Transaction;
import com.myworth.networthtracker.repository.AccountRepository;
import com.myworth.networthtracker.repository.CategoryRepository;
import com.myworth.networthtracker.repository.TransactionRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;

@Controller
public class NetWorthInputController {

    private final AccountRepository accountRepository;
    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;

    public NetWorthInputController(AccountRepository accountRepository, 
                                  CategoryRepository categoryRepository,
                                  TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.categoryRepository = categoryRepository;
        this.transactionRepository = transactionRepository;
    }

    @GetMapping("/networth-input")
    public String showNetWorthInputForm(Model model) {
        // Add accounts and categories to the model for dropdowns
        model.addAttribute("accounts", accountRepository.findAll());
        model.addAttribute("categories", categoryRepository.findAll());
        
        return "networth-input";
    }

    @PostMapping("/networth-input")
    public String addTransaction(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam("description") String description,
            @RequestParam("amount") BigDecimal amount,
            @RequestParam("transactionType") TransactionType transactionType,
            @RequestParam("accountId") Long accountId,
            @RequestParam("categoryId") Long categoryId,
            RedirectAttributes redirectAttributes) {
        
        try {
            Account account = accountRepository.findById(accountId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid account ID"));
            
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));
            
            Transaction transaction = new Transaction(
                    date, description, amount, transactionType, category, account);
            
            transactionRepository.save(transaction);
            
            redirectAttributes.addFlashAttribute("successMessage", 
                    "Transaction added successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                    "Error adding transaction: " + e.getMessage());
        }
        
        return "redirect:/networth-input";
    }

    @PostMapping("/account")
    public String createAccount(
            @RequestParam("name") String name,
            @RequestParam("type") AccountType type,
            RedirectAttributes redirectAttributes) {
        
        try {
            Account account = new Account(name, type);
            accountRepository.save(account);
            
            redirectAttributes.addFlashAttribute("successMessage", 
                    "Account created successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                    "Error creating account: " + e.getMessage());
        }
        
        return "redirect:/networth-input";
    }

    @PostMapping("/category")
    public String createCategory(
            @RequestParam("name") String name,
            RedirectAttributes redirectAttributes) {
        
        try {
            Category category = new Category(name);
            categoryRepository.save(category);
            
            redirectAttributes.addFlashAttribute("successMessage", 
                    "Category created successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                    "Error creating category: " + e.getMessage());
        }
        
        return "redirect:/networth-input";
    }
}