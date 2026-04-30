package com.udea.incomeservice.domain.usecase;

import com.udea.incomeservice.domain.exception.DomainConstants;
import com.udea.incomeservice.domain.exception.InvalidIncomeException;
import com.udea.incomeservice.domain.gateway.IncomeGateway;
import com.udea.incomeservice.domain.model.Income;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
public class IncomeUseCase {

    private final IncomeGateway incomeGateway;

    public Income registerIncome(Income income) {
        validateIncome(income);
        return incomeGateway.save(income);
    }

    public List<Income> getMonthlyIncomes(Long userId, int year, int month) {
        return incomeGateway.findByUserIdAndMonth(userId, year, month);
    }

    public List<Income> getAllIncomes(Long userId) {
        return incomeGateway.findByUserId(userId);
    }

    private void validateIncome(Income income) {
        if (income.getAmount() == null || income.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidIncomeException(DomainConstants.AMOUNT_MUST_BE_POSITIVE);
        }
        if (income.getCategory() == null || income.getCategory().isBlank()) {
            throw new InvalidIncomeException(DomainConstants.CATEGORY_REQUIRED);
        }
        if (income.getDate() == null) {
            throw new InvalidIncomeException(DomainConstants.DATE_REQUIRED);
        }
        if (income.getDescription() == null || income.getDescription().isBlank()) {
            throw new InvalidIncomeException(DomainConstants.DESCRIPTION_REQUIRED);
        }
    }
}