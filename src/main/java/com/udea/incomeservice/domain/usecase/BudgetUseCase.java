package com.udea.incomeservice.domain.usecase;

import com.udea.incomeservice.domain.exception.DomainConstants;
import com.udea.incomeservice.domain.exception.DuplicateBudgetException;
import com.udea.incomeservice.domain.exception.InvalidExpenseException;
import com.udea.incomeservice.domain.gateway.BudgetGateway;
import com.udea.incomeservice.domain.gateway.CategoryGateway;
import com.udea.incomeservice.domain.gateway.ExpenseGateway;
import com.udea.incomeservice.domain.model.Budget;
import com.udea.incomeservice.domain.model.BudgetStatus;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
public class BudgetUseCase {

    private static final double WARNING_THRESHOLD = 80.0;

    private final BudgetGateway budgetGateway;
    private final ExpenseGateway expenseGateway;
    private final CategoryGateway categoryGateway;

    public Budget createBudget(Budget budget) {
        if (budget.getMaxAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidExpenseException(DomainConstants.AMOUNT_MUST_BE_POSITIVE);
        }
        if (!categoryGateway.existsById(budget.getCategoryId())) {
            throw new InvalidExpenseException(DomainConstants.CATEGORY_NOT_FOUND);
        }
        if (budgetGateway.existsByUserIdAndCategoryId(budget.getUserId(), budget.getCategoryId())) {
            throw new DuplicateBudgetException(DomainConstants.BUDGET_ALREADY_EXISTS);
        }
        return budgetGateway.save(budget);
    }

    public List<BudgetStatus> getBudgetStatuses(Long userId) {
        LocalDate now = LocalDate.now();
        return budgetGateway.findByUserId(userId).stream()
                .map(b -> buildStatus(b, userId, now.getYear(), now.getMonthValue()))
                .toList();
    }

    public BudgetStatus checkBudgetAlert(Long userId, Long categoryId) {
        LocalDate now = LocalDate.now();
        return budgetGateway.findByUserIdAndCategoryId(userId, categoryId)
                .map(b -> buildStatus(b, userId, now.getYear(), now.getMonthValue()))
                .orElse(null);
    }

    private BudgetStatus buildStatus(Budget budget, Long userId, int year, int month) {
        BigDecimal spent = expenseGateway.sumByUserIdAndCategoryIdAndMonth(
                userId, budget.getCategoryId(), year, month);
        BigDecimal available = budget.getMaxAmount().subtract(spent);
        double percentage = spent.multiply(BigDecimal.valueOf(100))
                .divide(budget.getMaxAmount(), 2, RoundingMode.HALF_UP)
                .doubleValue();

        String status;
        if (percentage >= 100) {
            status = BudgetStatus.EXCEEDED;
        } else if (percentage >= WARNING_THRESHOLD) {
            status = BudgetStatus.WARNING;
        } else {
            status = BudgetStatus.OK;
        }

        return BudgetStatus.builder()
                .budgetId(budget.getId())
                .categoryName(budget.getCategoryName())
                .maxAmount(budget.getMaxAmount())
                .spentAmount(spent)
                .availableAmount(available)
                .percentage(percentage)
                .status(status)
                .build();
    }
}
