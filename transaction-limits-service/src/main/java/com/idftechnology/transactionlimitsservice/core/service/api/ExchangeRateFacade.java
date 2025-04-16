package com.idftechnology.transactionlimitsservice.core.service.api;

import com.idftechnology.transactionlimitsservice.core.service.dto.CurrencyPair;
import com.idftechnology.transactionlimitsservice.core.service.dto.ExchangeRateResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ExchangeRateFacade {

    List<ExchangeRateResponse> getRatesByPairForDates(Map<CurrencyPair, List<LocalDate>> allDatesByPair);
}
