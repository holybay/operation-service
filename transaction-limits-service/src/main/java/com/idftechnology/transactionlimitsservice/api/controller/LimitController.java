package com.idftechnology.transactionlimitsservice.api.controller;

import com.idftechnology.transactionlimitsservice.api.dto.LimitCreateDto;
import com.idftechnology.transactionlimitsservice.api.dto.LimitOutDto;
import com.idftechnology.transactionlimitsservice.core.platform.util.ApplicationConstant;
import com.idftechnology.transactionlimitsservice.core.service.api.LimitFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import org.springframework.web.bind.annotation.RestController;

import java.time.ZoneOffset;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/account/{accountId}/limits")
@Validated
@Tag(name = "Limits", description = "Операции по лимитам аккаунта")
public class LimitController {

    private final LimitFacade limitFacade;

    @Operation(summary = "Создать новый лимит", description = "Создаёт лимит на расходы по заданному аккаунту")
    @PostMapping
    public ResponseEntity<LimitOutDto> add(@PathVariable Long accountId,
                                           @RequestBody @Valid LimitCreateDto createDto,
                                           @RequestHeader(ApplicationConstant.ZONE_OFFSET) @NotNull
                                           ZoneOffset zoneOffset) {
        LimitOutDto created = limitFacade.add(accountId, createDto, zoneOffset);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @Operation(summary = "Получить все лимиты", description = "Возвращает список лимитов по аккаунту с пагинацией")
    @GetMapping
    public ResponseEntity<List<LimitOutDto>> getAll(@PathVariable Long accountId,
                                                    Pageable pageable,
                                                    @RequestHeader(ApplicationConstant.ZONE_OFFSET) @NotNull
                                                    ZoneOffset zoneOffset) {
        List<LimitOutDto> limits = limitFacade.getAll(accountId, pageable, zoneOffset);
        return new ResponseEntity<>(limits, HttpStatus.OK);
    }
}
