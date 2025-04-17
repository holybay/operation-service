package com.idftechnology.transactionlimitsservice.core.service.api;

import com.idftechnology.transactionlimitsservice.api.dto.TransactionCreateDto;
import com.idftechnology.transactionlimitsservice.api.dto.TransactionOutDto;
import org.springframework.data.domain.Pageable;

import java.time.ZoneOffset;
import java.util.List;

public interface TransactionService {

    TransactionOutDto create(TransactionCreateDto dto);

    List<TransactionOutDto> getAll(Long accountId, String expenseCategory, ZoneOffset zone, Pageable pageable);

}
