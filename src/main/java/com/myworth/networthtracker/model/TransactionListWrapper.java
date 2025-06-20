package com.myworth.networthtracker.model;

import java.util.ArrayList;
import java.util.List;

public class TransactionListWrapper {

    private List<Transaction> transactions;

    public TransactionListWrapper() {
        this.transactions = new ArrayList<>();
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    // Helper method to add a transaction easily
    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
    }
}
