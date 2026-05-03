package com.udea.incomeservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BudgetStatus {
    private Long budgetId;
    private String categoryName;
    private BigDecimal maxAmount;
    private BigDecimal spentAmount;
    private BigDecimal availableAmount;
    private double percentage;
    private String status;

    public static final String OK = "OK";
    public static final String WARNING = "WARNING";
    public static final String EXCEEDED = "EXCEEDED";
}
