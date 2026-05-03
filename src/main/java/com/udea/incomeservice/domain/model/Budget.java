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
public class Budget {
    private Long id;
    private Long userId;
    private Long categoryId;
    private String categoryName;
    private BigDecimal maxAmount;
}
