package com.idftechnology.transactionlimitsservice.core.service.impl;

import com.idftechnology.transactionlimitsservice.api.dto.LimitCreateDto;
import com.idftechnology.transactionlimitsservice.api.dto.LimitOutDto;
import com.idftechnology.transactionlimitsservice.core.service.api.LimitFacade;
import com.idftechnology.transactionlimitsservice.core.service.api.LimitInitializeService;
import com.idftechnology.transactionlimitsservice.core.service.api.LimitService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LimitFacadeImpl implements LimitFacade {

    private final LimitService limitService;
    private final LimitInitializeService limitInitService;

    @Override
    public LimitOutDto add(Long accountId, LimitCreateDto dto) {
        limitInitService.checkAndInit(accountId, dto.getZone());
        return limitService.add(accountId, dto);
    }

    @Override
    public List<LimitOutDto> getAll(Long accountId, Pageable pageable, ZoneOffset zoneOffset) {
        limitInitService.checkAndInit(accountId, zoneOffset);
        return limitService.getAll(accountId, pageable);
    }
}
