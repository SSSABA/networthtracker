package com.myworth.networthtracker.dto;

import java.util.ArrayList;
import java.util.List;

// Wrapper object for the whole form, holds a list of daily entry rows
public class ConsolidatedFormWrapper {
    private List<DailyEntryDTO> dailyEntries = new ArrayList<>();

    // Getters and Setters
    public List<DailyEntryDTO> getDailyEntries() { return dailyEntries; }
    public void setDailyEntries(List<DailyEntryDTO> dailyEntries) { this.dailyEntries = dailyEntries; }
}