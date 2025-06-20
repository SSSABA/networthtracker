package com.myworth.networthtracker.config;

import com.myworth.networthtracker.enums.AccountType;
import com.myworth.networthtracker.enums.TransactionType;
import com.myworth.networthtracker.model.Account;
import com.myworth.networthtracker.model.Category;
import com.myworth.networthtracker.model.Transaction;
import com.myworth.networthtracker.repository.AccountRepository;
import com.myworth.networthtracker.repository.CategoryRepository;
import com.myworth.networthtracker.repository.TransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public DataInitializer(CategoryRepository categoryRepository, AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.categoryRepository = categoryRepository;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // STEP 1: Clear all existing data to ensure a fresh start.
        // The order is important due to database relationships.
        // We must delete the data that "depends on" other data first.
        transactionRepository.deleteAll();
        accountRepository.deleteAll();
        categoryRepository.deleteAll();

        // STEP 2: Create the default Categories that you will use.
        // You can add or remove any of these as you see fit.
        categoryRepository.save(new Category("Salary"));
        categoryRepository.save(new Category("Groceries"));
        categoryRepository.save(new Category("Utilities"));
        categoryRepository.save(new Category("Rent"));
        categoryRepository.save(new Category("Dividends"));
        categoryRepository.save(new Category("Dining Out"));
        categoryRepository.save(new Category("Fuel"));
        categoryRepository.save(new Category("Entertainment"));
        categoryRepository.save(new Category("Shopping"));


        // STEP 3: Create the default Accounts.
        // These are your primary asset and liability accounts.
        accountRepository.save(new Account("Checking Account", AccountType.ASSET));
        accountRepository.save(new Account("Savings Account", AccountType.ASSET));
        accountRepository.save(new Account("Investment Portfolio", AccountType.ASSET));
        accountRepository.save(new Account("Credit Card", AccountType.LIABILITY));
        accountRepository.save(new Account("Personal Loan", AccountType.LIABILITY));

        // STEP 4: Transaction seeding is now REMOVED.
        // The transaction table will be empty on startup.
    }
}
