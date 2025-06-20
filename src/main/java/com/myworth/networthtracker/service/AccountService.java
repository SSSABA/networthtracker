package com.myworth.networthtracker.service;

import com.myworth.networthtracker.enums.AccountType;
import com.myworth.networthtracker.enums.TransactionType;
import com.myworth.networthtracker.model.Account;
import com.myworth.networthtracker.model.Transaction;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountService {

    // This method calculates the current balance of a single account
    public BigDecimal calculateBalance(Account account) {
        return calculateBalance(account, account.getTransactions());
    }

    // This is the new overloaded method for efficiency
    public BigDecimal calculateBalance(Account account, List<Transaction> transactions) {
        BigDecimal balance = BigDecimal.ZERO;

        // Filter transactions for only this account
        List<Transaction> accountTransactions = transactions.stream()
                .filter(t -> t.getAccount().getId().equals(account.getId()))
                .toList();

        for (Transaction t : accountTransactions) {
            // ... (rest of the logic is the same as your original method)
            if (account.getType() == AccountType.ASSET) {
                if (t.getType() == TransactionType.INCOME) { balance = balance.add(t.getAmount()); }
                else { balance = balance.subtract(t.getAmount()); }
            } else { // LIABILITY
                if (t.getType() == TransactionType.EXPENSE) { balance = balance.add(t.getAmount()); }
                else { balance = balance.subtract(t.getAmount()); }
            }
        }
        return balance;
    }
}
