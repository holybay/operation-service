package com.idftechnology.transactionlimitsservice.api.controller;

import com.idftechnology.transactionlimitsservice.api.dto.TransactionCreateDto;
import com.idftechnology.transactionlimitsservice.api.dto.TransactionOutDto;
import com.idftechnology.transactionlimitsservice.core.platform.util.ApplicationConstant;
import com.idftechnology.transactionlimitsservice.core.platform.validation.api.ExpenseCategoryValid;
import com.idftechnology.transactionlimitsservice.core.service.api.TransactionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZoneOffset;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/transactions")
@Validated
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/add")
    public ResponseEntity<TransactionOutDto> create(@RequestBody @Valid TransactionCreateDto createDto) {
        TransactionOutDto created = transactionService.create(createDto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/account/{accountId}/exceeded")
    public ResponseEntity<List<TransactionOutDto>> getExceeded(@PathVariable Long accountId,
                                                               @RequestParam("expense_category") @ExpenseCategoryValid
                                                               String expenseCategory,
                                                               @RequestHeader(ApplicationConstant.ZONE_OFFSET) @NotNull
                                                               ZoneOffset zoneOffset,
                                                               Pageable pageable) {
        List<TransactionOutDto> exceeded = transactionService.getAll(accountId, expenseCategory, zoneOffset, pageable);
        return new ResponseEntity<>(exceeded, HttpStatus.OK);

    }
}

