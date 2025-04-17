package com.idftechnology.transactionlimitsservice.core.service.impl;

import com.idftechnology.transactionlimitsservice.core.repository.api.ExchangeRateRepository;
import com.idftechnology.transactionlimitsservice.core.repository.entity.ExchangeRate;
import com.idftechnology.transactionlimitsservice.core.service.api.ExchangeRateService;
import com.idftechnology.transactionlimitsservice.core.service.dto.CurrencyPair;
import com.idftechnology.transactionlimitsservice.core.service.dto.ExchangeRateItem;
import com.idftechnology.transactionlimitsservice.core.service.dto.ExchangeRateItemResponse;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExchangeRateServiceImpl implements ExchangeRateService {

    private final ExchangeRateRepository repository;

    @Transactional
    @Override
    public void save(ExchangeRateItem item, @Nullable BigDecimal close, @Nullable BigDecimal previousClose) {
        if (Objects.isNull(close) && Objects.isNull(previousClose)) {
            throw new IllegalArgumentException("Both close and previousClose cannot be null");
        }
        repository.saveAndFlush(ExchangeRate.builder()
                                            .from(Currency.getInstance(item.getPair().getFrom()))
                                            .to(Currency.getInstance(item.getPair().getTo()))
                                            .date(item.getDate())
                                            .closeRate(close)
                                            .previousCloseRate(previousClose)
                                            .build());
    }

    @Transactional(readOnly = true)
    @Override
    public List<ExchangeRateItemResponse> getAllRatesForEachPairByDate(List<ExchangeRateItem> items) {

        Set<String> allFrom = new HashSet<>();
        Set<String> allTo = new HashSet<>();
        Set<LocalDate> dates = new HashSet<>();

        items.forEach(i -> {
            allFrom.add(i.getPair().getFrom());
            allTo.add(i.getPair().getTo());
            dates.add(i.getDate());
        });

        List<ExchangeRate> exchangeRates = repository.findRatesByPairsAndDates(allFrom, allTo, dates);

        return exchangeRates.stream()
                            .map(entity -> {
                                ExchangeRateItemResponse resp = ExchangeRateItemResponse.builder()
                                                                                        .build();
                                CurrencyPair pair = CurrencyPair.builder()
                                                                .from(entity.getFrom().getCurrencyCode())
                                                                .to(entity.getTo().getCurrencyCode())
                                                                .build();
                                ExchangeRateItem item = ExchangeRateItem.builder()
                                                                        .pair(pair)
                                                                        .date(entity.getDate())
                                                                        .build();
                                resp.setItem(item);
                                if (!Objects.isNull(entity.getCloseRate())) {
                                    resp.setRate(entity.getCloseRate());
                                } else {
                                    resp.setRate(entity.getPreviousCloseRate());
                                }
                                return resp;
                            })
                            .collect(Collectors.toList());
    }

}
