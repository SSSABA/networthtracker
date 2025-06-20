package com.myworth.networthtracker.controller;

import com.myworth.networthtracker.enums.TransactionType;
import com.myworth.networthtracker.model.Transaction;
import com.myworth.networthtracker.repository.AccountRepository;
import com.myworth.networthtracker.repository.CategoryRepository;
import com.myworth.networthtracker.repository.TransactionRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class TransactionController {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final AccountRepository accountRepository;

    public TransactionController(TransactionRepository transactionRepository, CategoryRepository categoryRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
        this.accountRepository = accountRepository;
    }

    // Handles GET request for /transactions page
    // Finds all transactions, sorts them by date descending, and adds them to the model
    @GetMapping("/transactions")
    public String listTransactions(Model model) {
        model.addAttribute("transactions", transactionRepository.findAll(Sort.by(Sort.Direction.DESC, "date")));
        return "transactions"; // Returns the transactions.html view
    }

    // Handles GET request for /add-transaction page
    // Prepares all the necessary data for the form's dropdown menus
    @GetMapping("/add-transaction")
    public String showAddTransactionForm(Model model) {
        model.addAttribute("transaction", new Transaction()); // For form binding
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("accounts", accountRepository.findAll());
        model.addAttribute("types", TransactionType.values());
        return "add-transaction"; // Returns the add-transaction.html view
    }

    // Handles POST request from the form at /add-transaction
    // Saves the new transaction and redirects to the dashboard to see the impact
    @PostMapping("/add-transaction")
    public String addTransaction(@ModelAttribute("transaction") Transaction transaction) {
        transactionRepository.save(transaction);
        return "redirect:/"; // Redirect to the dashboard page
    }
}
