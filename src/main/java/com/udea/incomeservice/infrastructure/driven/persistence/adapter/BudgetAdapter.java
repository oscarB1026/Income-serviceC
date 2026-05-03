package com.udea.incomeservice.infrastructure.driven.persistence.adapter;

import com.udea.incomeservice.domain.gateway.BudgetGateway;
import com.udea.incomeservice.domain.model.Budget;
import com.udea.incomeservice.infrastructure.driven.persistence.mapper.BudgetEntityMapper;
import com.udea.incomeservice.infrastructure.driven.persistence.repository.BudgetJpaRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BudgetAdapter implements BudgetGateway {

    private final BudgetJpaRepository repository;
    private final BudgetEntityMapper mapper;
    private final EntityManager entityManager;

    @Override
    @Transactional
    public Budget save(Budget budget) {
        var saved = repository.save(mapper.toEntity(budget));
        entityManager.refresh(saved);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Budget> findByUserIdAndCategoryId(Long userId, Long categoryId) {
        return repository.findByUserIdAndCategoryId(userId, categoryId)
                .map(mapper::toDomain);
    }

    @Override
    public List<Budget> findByUserId(Long userId) {
        return repository.findByUserId(userId).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsByUserIdAndCategoryId(Long userId, Long categoryId) {
        return repository.existsByUserIdAndCategoryId(userId, categoryId);
    }
}
