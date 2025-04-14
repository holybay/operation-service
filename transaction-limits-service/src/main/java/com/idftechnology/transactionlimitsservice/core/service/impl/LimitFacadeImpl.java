package com.idftechnology.transactionlimitsservice.core.service.impl;

import com.idftechnology.transactionlimitsservice.api.dto.LimitCreateDto;
import com.idftechnology.transactionlimitsservice.api.dto.LimitOutDto;
import com.idftechnology.transactionlimitsservice.core.service.api.LimitFacade;
import com.idftechnology.transactionlimitsservice.core.service.api.LimitInitializeService;
import com.idftechnology.transactionlimitsservice.core.service.api.LimitService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

}
