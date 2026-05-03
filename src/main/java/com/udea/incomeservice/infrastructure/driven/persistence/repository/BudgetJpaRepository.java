package com.udea.incomeservice.infrastructure.driven.persistence.repository;

import com.udea.incomeservice.infrastructure.driven.persistence.entity.BudgetEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BudgetJpaRepository extends JpaRepository<BudgetEntity, Long> {
    Optional<BudgetEntity> findByUserIdAndCategoryId(Long userId, Long categoryId);
    List<BudgetEntity> findByUserId(Long userId);
    boolean existsByUserIdAndCategoryId(Long userId, Long categoryId);
}
