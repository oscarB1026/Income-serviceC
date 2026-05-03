package com.udea.incomeservice.infrastructure.entrypoint.rest.controller;

import com.udea.incomeservice.domain.exception.DomainConstants;
import com.udea.incomeservice.domain.model.BudgetStatus;
import com.udea.incomeservice.domain.usecase.BudgetUseCase;
import com.udea.incomeservice.domain.usecase.ExpenseUseCase;
import com.udea.incomeservice.infrastructure.entrypoint.rest.EntryPointConstants;
import com.udea.incomeservice.infrastructure.entrypoint.rest.dto.ExpenseRequestDTO;
import com.udea.incomeservice.infrastructure.entrypoint.rest.dto.ExpenseResponseDTO;
import com.udea.incomeservice.infrastructure.entrypoint.rest.mapper.ExpenseRestMapper;
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
@RequestMapping(EntryPointConstants.EXPENSE_BASE_PATH)
@RequiredArgsConstructor
@Tag(name = "Expenses", description = "Gestión de gastos financieros")
@SecurityRequirement(name = "bearerAuth")
public class ExpenseController {

    private final ExpenseUseCase expenseUseCase;
    private final BudgetUseCase budgetUseCase;
    private final ExpenseRestMapper mapper;

    @PostMapping
    @Operation(summary = "Registrar un gasto")
    public ResponseEntity<ExpenseResponseDTO> registerExpense(
            @Valid @RequestBody ExpenseRequestDTO request,
            Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        var expense = expenseUseCase.registerExpense(mapper.toDomain(request, userId));
        var response = mapper.toResponse(expense);

        BudgetStatus status = budgetUseCase.checkBudgetAlert(userId, expense.getCategoryId());
        if (status != null) {
            String alert = buildAlert(status);
            response = ExpenseResponseDTO.builder()
                    .id(response.getId())
                    .amount(response.getAmount())
                    .description(response.getDescription())
                    .date(response.getDate())
                    .categoryName(response.getCategoryName())
                    .budgetAlert(alert)
                    .createdAt(response.getCreatedAt())
                    .build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(EntryPointConstants.USER_PATH)
    @Operation(summary = "Listar todos los gastos de un usuario")
    public ResponseEntity<List<ExpenseResponseDTO>> getAllExpenses(
            @PathVariable Long userId) {
        var expenses = expenseUseCase.getAllExpenses(userId)
                .stream()
                .map(mapper::toResponse)
                .toList();
        return ResponseEntity.ok(expenses);
    }

    @GetMapping(EntryPointConstants.USER_MONTHLY_PATH)
    @Operation(summary = "Listar gastos del mes")
    public ResponseEntity<List<ExpenseResponseDTO>> getMonthlyExpenses(
            @PathVariable Long userId,
            @RequestParam int year,
            @RequestParam int month) {
        var expenses = expenseUseCase.getMonthlyExpenses(userId, year, month)
                .stream()
                .map(mapper::toResponse)
                .toList();
        return ResponseEntity.ok(expenses);
    }

    private String buildAlert(BudgetStatus status) {
        if (BudgetStatus.EXCEEDED.equals(status.getStatus())) {
            return DomainConstants.BUDGET_EXCEEDED;
        }
        if (BudgetStatus.WARNING.equals(status.getStatus())) {
            return String.format(DomainConstants.BUDGET_WARNING, status.getCategoryName());
        }
        return null;
    }
}
