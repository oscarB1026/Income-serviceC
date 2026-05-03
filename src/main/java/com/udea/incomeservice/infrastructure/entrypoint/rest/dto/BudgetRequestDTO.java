package com.udea.incomeservice.infrastructure.entrypoint.rest.dto;

import com.udea.incomeservice.infrastructure.entrypoint.rest.EntryPointConstants;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class BudgetRequestDTO {

    @NotNull(message = EntryPointConstants.CATEGORY_REQUIRED)
    private Long categoryId;

    @NotNull(message = EntryPointConstants.AMOUNT_REQUIRED)
    @DecimalMin(value = EntryPointConstants.AMOUNT_MIN, message = EntryPointConstants.AMOUNT_MUST_BE_POSITIVE)
    private BigDecimal maxAmount;
}
