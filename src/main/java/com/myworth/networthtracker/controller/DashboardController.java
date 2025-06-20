package com.myworth.networthtracker.controller;

import com.myworth.networthtracker.repository.AccountRepository;
import com.myworth.networthtracker.service.DashboardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Controller
public class DashboardController {

    private final DashboardService dashboardService;
    private final AccountRepository accountRepository;

    public DashboardController(DashboardService dashboardService, AccountRepository accountRepository) {
        this.dashboardService = dashboardService;
        this.accountRepository = accountRepository;
    }

    @GetMapping("/")
    public String getDashboard(Model model) {
        // Basic financial data
        model.addAttribute("netWorth", dashboardService.calculateNetWorth());
        model.addAttribute("totalAssets", dashboardService.calculateTotalAssets());
        model.addAttribute("totalLiabilities", dashboardService.calculateTotalLiabilities());

        // Account list
        model.addAttribute("accounts", accountRepository.findAll());

        // Chart data (for now, we'll use dummy data)
        // In a real app, this would come from historical transaction data
        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMM");
        List<String> months = Arrays.asList(
                LocalDate.now().minusMonths(5).format(monthFormatter),
                LocalDate.now().minusMonths(4).format(monthFormatter),
                LocalDate.now().minusMonths(3).format(monthFormatter),
                LocalDate.now().minusMonths(2).format(monthFormatter),
                LocalDate.now().minusMonths(1).format(monthFormatter),
                LocalDate.now().format(monthFormatter)
        );
        model.addAttribute("chartLabels", months);

        // Sample net worth values for the last 6 months
        List<Double> netWorthData = Arrays.asList(10000.0, 12000.0, 11500.0, 13000.0, 14500.0, 15000.0);
        model.addAttribute("chartData", netWorthData);

        return "dashboard"; // This will look for a file named "dashboard.html"
    }
}
