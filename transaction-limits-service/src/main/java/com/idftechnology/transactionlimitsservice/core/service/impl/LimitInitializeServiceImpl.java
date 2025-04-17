package com.idftechnology.transactionlimitsservice.core.service.impl;

import com.idftechnology.transactionlimitsservice.core.config.property.LimitInitProperty;
import com.idftechnology.transactionlimitsservice.core.service.api.AccountRecordService;
import com.idftechnology.transactionlimitsservice.core.service.api.LimitInitializeService;
import com.idftechnology.transactionlimitsservice.core.service.api.LimitService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;

@Service
@RequiredArgsConstructor
public class LimitInitializeServiceImpl implements LimitInitializeService {

    private final AccountRecordService accountRecordService;
    private final LimitService limitService;
    private final LimitInitProperty property;

    @Override
    public void checkAndInit(Long accountId, @NotNull ZoneOffset zone) {
        if (!accountRecordService.isActive(accountId)) {
            accountRecordService.activate(accountId);
            limitService.initiate(accountId, property.getLimitsByDefault(), zone);
        }
    }
}
