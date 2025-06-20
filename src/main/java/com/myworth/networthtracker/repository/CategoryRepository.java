package com.myworth.networthtracker.repository;

import com.myworth.networthtracker.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}