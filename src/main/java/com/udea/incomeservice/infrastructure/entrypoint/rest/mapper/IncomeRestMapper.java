package com.udea.incomeservice.infrastructure.entrypoint.rest.mapper;

import com.udea.incomeservice.domain.model.Income;
import com.udea.incomeservice.infrastructure.entrypoint.rest.dto.IncomeRequestDTO;
import com.udea.incomeservice.infrastructure.entrypoint.rest.dto.IncomeResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class IncomeRestMapper {

    public Income toDomain(IncomeRequestDTO dto, Long userId) {
        return Income.builder()
                .userId(userId)
                .amount(dto.getAmount())
                .description(dto.getDescription())
                .date(dto.getDate())
                .category(dto.getCategory())
                .build();
    }

    public IncomeResponseDTO toResponse(Income income) {
        return IncomeResponseDTO.builder()
                .id(income.getId())
                .userId(income.getUserId())
                .amount(income.getAmount())
                .description(income.getDescription())
                .date(income.getDate())
                .category(income.getCategory())
                .createdAt(income.getCreatedAt())
                .build();
    }
}
