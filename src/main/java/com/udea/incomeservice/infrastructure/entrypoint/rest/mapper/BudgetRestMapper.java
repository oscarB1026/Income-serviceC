package com.udea.incomeservice.infrastructure.entrypoint.rest.mapper;

import com.udea.incomeservice.domain.exception.DomainConstants;
import com.udea.incomeservice.domain.model.Budget;
import com.udea.incomeservice.domain.model.BudgetStatus;
import com.udea.incomeservice.infrastructure.entrypoint.rest.dto.BudgetRequestDTO;
import com.udea.incomeservice.infrastructure.entrypoint.rest.dto.BudgetStatusDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface BudgetRestMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "categoryName", ignore = true)
    @Mapping(target = "userId", source = "userId")
    Budget toDomain(BudgetRequestDTO dto, Long userId);

    @Mapping(target = "alert", source = ".", qualifiedByName = "toAlert")
    BudgetStatusDTO toStatusDTO(BudgetStatus status);

    @Named("toAlert")
    default String toAlert(BudgetStatus status) {
        if (BudgetStatus.EXCEEDED.equals(status.getStatus())) {
            return DomainConstants.BUDGET_EXCEEDED;
        }
        if (BudgetStatus.WARNING.equals(status.getStatus())) {
            return String.format(DomainConstants.BUDGET_WARNING, status.getCategoryName());
        }
        return null;
    }
}
