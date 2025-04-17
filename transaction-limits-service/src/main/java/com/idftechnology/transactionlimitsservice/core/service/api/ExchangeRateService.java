package com.idftechnology.transactionlimitsservice.core.service.api;

import com.idftechnology.transactionlimitsservice.core.service.dto.ExchangeRateItem;
import com.idftechnology.transactionlimitsservice.core.service.dto.ExchangeRateItemResponse;
import jakarta.annotation.Nullable;

import java.math.BigDecimal;
import java.util.List;

public interface ExchangeRateService {

    void save(ExchangeRateItem item, @Nullable BigDecimal close, @Nullable BigDecimal previousClose);

    List<ExchangeRateItemResponse> getAllRatesForEachPairByDate(List<ExchangeRateItem> items);
}
