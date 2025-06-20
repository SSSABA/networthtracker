package com.myworth.networthtracker.model;

import com.myworth.networthtracker.enums.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    // This field links a category to its parent (e.g., "Costco" -> "Groceries")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parentCategory;

    // This field holds all children of a category (e.g., "Groceries" -> ["Costco", "Whole Foods"])
    @OneToMany(mappedBy = "parentCategory", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Category> subCategories = new HashSet<>();

    // Every category now has a type (INCOME or EXPENSE)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType transactionType;

    // Every category is now linked to a default account
    @ManyToOne
    @JoinColumn(name = "default_account_id", nullable = false)
    private Account defaultAccount;

    // --- Constructors, Getters, and Setters ---
    public Category() {}

    public Category(String name, TransactionType transactionType, Account defaultAccount) {
        this.name = name;
        this.transactionType = transactionType;
        this.defaultAccount = defaultAccount;
    }
}
