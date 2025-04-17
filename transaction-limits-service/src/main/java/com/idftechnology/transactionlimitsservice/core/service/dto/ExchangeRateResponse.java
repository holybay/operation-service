package com.idftechnology.transactionlimitsservice.core.service.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Data
@Builder
public class ExchangeRateResponse {

    private CurrencyPair pair;

    private Map<LocalDate, BigDecimal> rates;

}
