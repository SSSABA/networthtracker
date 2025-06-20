package com.myworth.networthtracker.controller;

import com.myworth.networthtracker.dto.ChartData;
import com.myworth.networthtracker.service.DashboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard") // All URLs in this controller will start with /api/dashboard
public class DashboardApiController {

    private final DashboardService dashboardService;

    public DashboardApiController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/asset-liability-summary")
    public ChartData getAssetLiabilitySummary() {
        return dashboardService.getAssetLiabilityChartData();
    }

    @GetMapping("/networth-history")
    public ChartData getNetWorthHistory() {
        return dashboardService.getNetWorthHistoryChartData();
    }
}
