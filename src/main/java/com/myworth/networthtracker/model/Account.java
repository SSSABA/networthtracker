package com.myworth.networthtracker.model;

import com.myworth.networthtracker.enums.AccountType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private AccountType type; // ASSET or LIABILITY

    // We don't store the balance here. It will be calculated.

    @OneToMany(mappedBy = "account")
    private List<Transaction> transactions;

    // Custom constructor for name and type
    public Account(String name, AccountType type) {
        this.name = name;
        this.type = type;
    }
}
