package com.idftechnology.transactionlimitsservice.api.controller;

import com.idftechnology.transactionlimitsservice.api.dto.TransactionCreateDto;
import com.idftechnology.transactionlimitsservice.api.dto.TransactionOutDto;
import com.idftechnology.transactionlimitsservice.core.service.api.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/add")
    public ResponseEntity<TransactionOutDto> create(@RequestBody @Valid TransactionCreateDto createDto) {
        TransactionOutDto created = transactionService.create(createDto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

}

