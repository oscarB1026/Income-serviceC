package com.udea.incomeservice.infrastructure.driven.persistence.repository;

import com.udea.incomeservice.infrastructure.driven.persistence.entity.IncomeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface IncomeJpaRepository extends JpaRepository<IncomeEntity, Long> {

    List<IncomeEntity> findByUserId(Long userId);

    List<IncomeEntity> findByUserIdAndDateBetween(Long userId, LocalDate startDate, LocalDate endDate);
}
