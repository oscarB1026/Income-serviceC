package com.udea.incomeservice.domain.gateway;

import com.udea.incomeservice.domain.model.Budget;

import java.util.List;
import java.util.Optional;

public interface BudgetGateway {
    Budget save(Budget budget);
    Optional<Budget> findByUserIdAndCategoryId(Long userId, Long categoryId);
    List<Budget> findByUserId(Long userId);
    boolean existsByUserIdAndCategoryId(Long userId, Long categoryId);
}
