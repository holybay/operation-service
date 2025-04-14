package com.idftechnology.transactionlimitsservice.core.service.api;

import com.idftechnology.transactionlimitsservice.api.dto.LimitCreateDto;
import com.idftechnology.transactionlimitsservice.api.dto.LimitOutDto;
import org.springframework.data.domain.Pageable;

import java.time.ZoneOffset;
import java.util.List;

public interface LimitFacade {

    LimitOutDto add(Long accountId, LimitCreateDto dto);

    List<LimitOutDto> getAll(Long accountId, Pageable pageable, ZoneOffset zoneOffset);

}
