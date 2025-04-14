package com.idftechnology.transactionlimitsservice.core.service.api;

import jakarta.validation.constraints.NotNull;

import java.time.ZoneOffset;

public interface LimitInitializeService {

    void checkAndInit(Long accountId, @NotNull ZoneOffset zone);

}

