package com.idftechnology.transactionlimitsservice.core.service.api;

import com.idftechnology.transactionlimitsservice.api.dto.TransactionCreateDto;
import com.idftechnology.transactionlimitsservice.api.dto.TransactionOutDto;

public interface TransactionService {

    TransactionOutDto create(TransactionCreateDto dto);
}
