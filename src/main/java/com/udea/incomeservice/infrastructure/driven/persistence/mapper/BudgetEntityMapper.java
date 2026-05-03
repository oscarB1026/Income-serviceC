package com.udea.incomeservice.infrastructure.driven.persistence.mapper;

import com.udea.incomeservice.domain.model.Budget;
import com.udea.incomeservice.infrastructure.driven.persistence.entity.BudgetEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BudgetEntityMapper {

    @Mapping(target = "category.id", source = "categoryId")
    BudgetEntity toEntity(Budget budget);

    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "categoryName", source = "category.name")
    Budget toDomain(BudgetEntity entity);
}
