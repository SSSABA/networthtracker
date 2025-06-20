package com.myworth.networthtracker.repository;

import com.myworth.networthtracker.enums.AccountType;
import com.myworth.networthtracker.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
    // Spring Data JPA will automatically implement this method for us!
    List<Account> findByType(AccountType type);
}