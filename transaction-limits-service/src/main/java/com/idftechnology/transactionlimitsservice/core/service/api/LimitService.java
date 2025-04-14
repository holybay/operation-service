package com.idftechnology.transactionlimitsservice.core.service.api;

import com.idftechnology.transactionlimitsservice.api.dto.LimitCreateDto;
import com.idftechnology.transactionlimitsservice.api.dto.LimitOutDto;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.ZoneOffset;
import java.util.Map;

public interface LimitService {

    void initiate(Long accountId, Map<String, BigDecimal> limitsByDefault, @NotNull ZoneOffset zone);

    LimitOutDto add(Long accountId, LimitCreateDto dto);

}
