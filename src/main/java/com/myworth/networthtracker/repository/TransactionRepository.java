package com.myworth.networthtracker.repository;

import com.myworth.networthtracker.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findAllByOrderByDateAsc(); // This finds all transactions and sorts them by date

    long countByCategory_Id(Long categoryId);
}
