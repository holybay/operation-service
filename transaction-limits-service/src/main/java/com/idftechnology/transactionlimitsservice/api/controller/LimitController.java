package com.idftechnology.transactionlimitsservice.api.controller;

import com.idftechnology.transactionlimitsservice.api.dto.LimitCreateDto;
import com.idftechnology.transactionlimitsservice.api.dto.LimitOutDto;
import com.idftechnology.transactionlimitsservice.core.platform.util.ApplicationConstant;
import com.idftechnology.transactionlimitsservice.core.service.api.LimitFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZoneOffset;
import java.util.List;

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

    @GetMapping
    public ResponseEntity<List<LimitOutDto>> getAll(@PathVariable Long accountId,
                                                    Pageable pageable,
                                                    @RequestHeader(ApplicationConstant.ZONE_OFFSET)
                                                    ZoneOffset zoneOffset) {
        List<LimitOutDto> limits = limitFacade.getAll(accountId, pageable, zoneOffset);
        return new ResponseEntity<>(limits, HttpStatus.OK);
    }
}
