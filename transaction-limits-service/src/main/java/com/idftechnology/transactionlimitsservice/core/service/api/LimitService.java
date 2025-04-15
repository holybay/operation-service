package com.idftechnology.transactionlimitsservice.core.service.api;

import com.idftechnology.transactionlimitsservice.api.dto.LimitCreateDto;
import com.idftechnology.transactionlimitsservice.api.dto.LimitOutDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;

public interface LimitService {

    void initiate(Long accountId, Map<String, BigDecimal> limitsByDefault, @NotNull ZoneOffset zone);

    LimitOutDto add(Long accountId, LimitCreateDto dto, @NotBlank ZoneOffset zoneOffset);

    List<LimitOutDto> getAll(Long accountId, Pageable pageable);

}
