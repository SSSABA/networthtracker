package com.myworth.networthtracker.dto;

import org.springframework.format.annotation.DateTimeFormat;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

// Represents a single row (a single date) on our form
public class DailyEntryDTO {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    // Key: Sub-Category ID, Value: Amount entered
    private Map<Long, BigDecimal> amounts = new HashMap<>();

    // Getters and Setters
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public Map<Long, BigDecimal> getAmounts() { return amounts; }
    public void setAmounts(Map<Long, BigDecimal> amounts) { this.amounts = amounts; }
}
