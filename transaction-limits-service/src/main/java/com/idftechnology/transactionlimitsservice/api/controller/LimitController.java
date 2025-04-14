package com.idftechnology.transactionlimitsservice.api.controller;

import com.idftechnology.transactionlimitsservice.api.dto.LimitCreateDto;
import com.idftechnology.transactionlimitsservice.api.dto.LimitOutDto;
import com.idftechnology.transactionlimitsservice.core.service.api.LimitFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("account/{accountId}/limits")
public class LimitController {

    private final LimitFacade limitFacade;

    @PostMapping
    public ResponseEntity<LimitOutDto> add(@PathVariable Long accountId,
                                           @RequestBody @Valid LimitCreateDto createDto) {
        LimitOutDto created = limitFacade.add(accountId, createDto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }
}
