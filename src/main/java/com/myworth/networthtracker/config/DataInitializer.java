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
import java.util.Set;

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
        transactionRepository.deleteAll();
        categoryRepository.deleteAll();
        accountRepository.deleteAll();

        // --- Create Accounts ---
        Account checking = accountRepository.save(new Account("Checking Account", AccountType.ASSET));
        Account savings = accountRepository.save(new Account("Savings Account", AccountType.ASSET));
        Account investments = accountRepository.save(new Account("Investment Portfolio", AccountType.ASSET));
        Account creditCard = accountRepository.save(new Account("Credit Card", AccountType.LIABILITY));

        // --- Create Parent Categories ---
        Category salary = categoryRepository.save(new Category("Salary", TransactionType.INCOME, checking));
        Category investmentIncome = categoryRepository.save(new Category("Investment Income", TransactionType.INCOME, investments));
        Category housing = categoryRepository.save(new Category("Housing", TransactionType.EXPENSE, checking));
        Category groceries = categoryRepository.save(new Category("Groceries", TransactionType.EXPENSE, creditCard));

        // --- Create Sub-Categories ---
        Category paycheck = new Category("Paycheck", salary.getTransactionType(), salary.getDefaultAccount());
        paycheck.setParentCategory(salary);

        Category dividends = new Category("Dividends", investmentIncome.getTransactionType(), investmentIncome.getDefaultAccount());
        dividends.setParentCategory(investmentIncome);

        Category rent = new Category("Rent", housing.getTransactionType(), housing.getDefaultAccount());
        rent.setParentCategory(housing);

        Category costco = new Category("Costco", groceries.getTransactionType(), groceries.getDefaultAccount());
        costco.setParentCategory(groceries);

        Category wholeFoods = new Category("Whole Foods", groceries.getTransactionType(), groceries.getDefaultAccount());
        wholeFoods.setParentCategory(groceries);

        categoryRepository.saveAll(Set.of(paycheck, dividends, rent, costco, wholeFoods));
    }
}
