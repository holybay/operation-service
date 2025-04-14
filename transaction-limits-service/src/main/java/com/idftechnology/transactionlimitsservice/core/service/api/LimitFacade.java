package com.idftechnology.transactionlimitsservice.core.service.api;

import com.idftechnology.transactionlimitsservice.api.dto.LimitCreateDto;
import com.idftechnology.transactionlimitsservice.api.dto.LimitOutDto;

public interface LimitFacade {

    LimitOutDto add(Long accountId, LimitCreateDto dto);

}
