package com.myworth.networthtracker;

import java.util.List;

// Using a record is a concise way to create an immutable data carrier class
public record ChartData(List<String> labels, List<Object> values) {
}
