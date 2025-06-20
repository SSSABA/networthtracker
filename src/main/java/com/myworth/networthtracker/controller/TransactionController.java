package com.myworth.networthtracker.controller;

import com.myworth.networthtracker.enums.TransactionType;
import com.myworth.networthtracker.model.Transaction;
import com.myworth.networthtracker.model.TransactionListWrapper;
import com.myworth.networthtracker.repository.AccountRepository;
import com.myworth.networthtracker.repository.CategoryRepository;
import com.myworth.networthtracker.repository.TransactionRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.stream.Collectors;


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
    // This now serves the new multi-entry page
    @GetMapping("/add-transactions") // Renamed URL for clarity
    public String showBulkAddTransactionForm(Model model) {
        TransactionListWrapper wrapper = new TransactionListWrapper();
        // Start with one empty transaction row
        wrapper.addTransaction(new Transaction());

        model.addAttribute("formWrapper", wrapper); // Use the wrapper as the model attribute
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("accounts", accountRepository.findAll());
        model.addAttribute("types", TransactionType.values());
        return "add-transactions-bulk"; // Point to a new HTML file
    }

    // Handles POST request from the form at /add-transaction
    // MODIFIED: This now saves a list of transactions
    @PostMapping("/add-transactions") // Renamed URL for clarity
    public String saveBulkTransactions(@ModelAttribute TransactionListWrapper formWrapper) {
        // Filter out any empty/incomplete rows that might have been submitted
        var transactionsToSave = formWrapper.getTransactions().stream()
                .filter(t -> t.getAmount() != null && t.getCategory() != null && t.getAccount() != null)
                .collect(Collectors.toList());

        if (!transactionsToSave.isEmpty()) {
            transactionRepository.saveAll(transactionsToSave);
        }
        return "redirect:/transactions"; // Redirect to the transaction list to see them all
    }
}
