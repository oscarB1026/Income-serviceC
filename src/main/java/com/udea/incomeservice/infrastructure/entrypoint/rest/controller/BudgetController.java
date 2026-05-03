package com.udea.incomeservice.infrastructure.entrypoint.rest.controller;

import com.udea.incomeservice.domain.usecase.BudgetUseCase;
import com.udea.incomeservice.infrastructure.entrypoint.rest.EntryPointConstants;
import com.udea.incomeservice.infrastructure.entrypoint.rest.dto.BudgetRequestDTO;
import com.udea.incomeservice.infrastructure.entrypoint.rest.dto.BudgetStatusDTO;
import com.udea.incomeservice.infrastructure.entrypoint.rest.mapper.BudgetRestMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(EntryPointConstants.BUDGET_BASE_PATH)
@RequiredArgsConstructor
@Tag(name = "Budgets", description = "Gestión de presupuestos por categoría")
@SecurityRequirement(name = "bearerAuth")
public class BudgetController {

    private final BudgetUseCase budgetUseCase;
    private final BudgetRestMapper mapper;

    @PostMapping
    @Operation(summary = "Crear presupuesto mensual para una categoría")
    public ResponseEntity<BudgetStatusDTO> createBudget(
            @Valid @RequestBody BudgetRequestDTO request,
            Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        var budget = budgetUseCase.createBudget(mapper.toDomain(request, userId));
        var status = budgetUseCase.checkBudgetAlert(userId, budget.getCategoryId());
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toStatusDTO(status));
    }

    @GetMapping
    @Operation(summary = "Consultar estado de todos los presupuestos del mes actual")
    public ResponseEntity<List<BudgetStatusDTO>> getBudgetStatuses(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        var statuses = budgetUseCase.getBudgetStatuses(userId).stream()
                .map(mapper::toStatusDTO)
                .toList();
        return ResponseEntity.ok(statuses);
    }
}
