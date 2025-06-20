package com.myworth.networthtracker.service;

import com.myworth.networthtracker.dto.ChartData;
import com.myworth.networthtracker.enums.AccountType;
import com.myworth.networthtracker.model.Transaction;
import com.myworth.networthtracker.repository.AccountRepository;
import com.myworth.networthtracker.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository; // Add this
    private final AccountService accountService;

    // Update constructor
    public DashboardService(AccountRepository accountRepository, AccountService accountService, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.accountService = accountService;
        this.transactionRepository = transactionRepository;
    }

    // Calculate total assets by summing all asset account balances
    public BigDecimal calculateTotalAssets() {
        return accountRepository.findByType(AccountType.ASSET).stream()
                .map(account -> accountService.calculateBalance(account))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Calculate total liabilities by summing all liability account balances
    public BigDecimal calculateTotalLiabilities() {
        return accountRepository.findByType(AccountType.LIABILITY).stream()
                .map(account -> accountService.calculateBalance(account))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Calculate net worth as assets minus liabilities
    public BigDecimal calculateNetWorth() {
        return calculateTotalAssets().subtract(calculateTotalLiabilities());
    }

    // --- NEW METHOD FOR PIE CHART ---
    public ChartData getAssetLiabilityChartData() {
        List<String> labels = List.of("Total Assets", "Total Liabilities");
        List<Object> values = List.of(calculateTotalAssets(), calculateTotalLiabilities());
        return new ChartData(labels, values);
    }

    // --- NEW METHOD FOR LINE CHART ---
    public ChartData getNetWorthHistoryChartData() {
        List<Transaction> allTransactions = transactionRepository.findAllByOrderByDateAsc(); // Need to create this method
        if (allTransactions.isEmpty()) {
            return new ChartData(List.of(), List.of());
        }

        // Group transactions by month
        Map<YearMonth, List<Transaction>> transactionsByMonth = allTransactions.stream()
                .collect(Collectors.groupingBy(t -> YearMonth.from(t.getDate())));

        // Get sorted list of months
        List<YearMonth> sortedMonths = transactionsByMonth.keySet().stream().sorted().collect(Collectors.toList());

        List<String> labels = new ArrayList<>();
        List<Object> values = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM yyyy");

        // Calculate cumulative net worth month by month
        for (YearMonth month : sortedMonths) {
            LocalDate endOfMonth = month.atEndOfMonth();

            // Consider all transactions up to the end of the current month
            List<Transaction> transactionsUpToMonth = allTransactions.stream()
                    .filter(t -> !t.getDate().isAfter(endOfMonth))
                    .toList();

            BigDecimal monthlyAssets = calculateBalanceForType(AccountType.ASSET, transactionsUpToMonth);
            BigDecimal monthlyLiabilities = calculateBalanceForType(AccountType.LIABILITY, transactionsUpToMonth);
            BigDecimal netWorth = monthlyAssets.subtract(monthlyLiabilities);

            labels.add(month.format(formatter));
            values.add(netWorth);
        }

        return new ChartData(labels, values);
    }

    // Helper method to calculate balance for a specific type based on a list of transactions
    private BigDecimal calculateBalanceForType(AccountType type, List<Transaction> transactions) {
        return accountRepository.findByType(type).stream()
                .map(account -> accountService.calculateBalance(account, transactions)) // Overload calculateBalance
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
