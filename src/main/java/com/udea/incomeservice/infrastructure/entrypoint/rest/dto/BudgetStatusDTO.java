package com.udea.incomeservice.infrastructure.entrypoint.rest.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class BudgetStatusDTO {
    private Long budgetId;
    private String categoryName;
    private BigDecimal maxAmount;
    private BigDecimal spentAmount;
    private BigDecimal availableAmount;
    private double percentage;
    private String status;
    private String alert;
}
