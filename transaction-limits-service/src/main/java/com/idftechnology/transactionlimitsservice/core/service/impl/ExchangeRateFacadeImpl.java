package com.idftechnology.transactionlimitsservice.core.service.impl;

import com.idftechnology.transactionlimitsservice.core.config.property.TwelveDataProperty;
import com.idftechnology.transactionlimitsservice.core.platform.exception.InvalidCurrencyResponseException;
import com.idftechnology.transactionlimitsservice.core.platform.util.CurrencyPairUtil;
import com.idftechnology.transactionlimitsservice.core.service.api.ExchangeRateFacade;
import com.idftechnology.transactionlimitsservice.core.service.api.ExchangeRateService;
import com.idftechnology.transactionlimitsservice.core.service.dto.CurrencyPair;
import com.idftechnology.transactionlimitsservice.core.service.dto.CurrencyResponseDto;
import com.idftechnology.transactionlimitsservice.core.service.dto.ExchangeRateItem;
import com.idftechnology.transactionlimitsservice.core.service.dto.ExchangeRateItemResponse;
import com.idftechnology.transactionlimitsservice.core.service.dto.ExchangeRateResponse;
import com.idftechnology.transactionlimitsservice.core.service.feign.client.TwelveDataClient;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExchangeRateFacadeImpl implements ExchangeRateFacade {

    private final ExchangeRateService exchangeService;
    private final TwelveDataClient currencyClient;
    private final TwelveDataProperty currencyClientProperty;
    private final Validator validator;
    private final CurrencyPairUtil currencyPairUtil;

    @Override
    public List<ExchangeRateResponse> getRatesByPairForDates(Map<CurrencyPair, List<LocalDate>> allDatesByPair) {

        List<ExchangeRateItem> requestedItems =
                allDatesByPair.entrySet()
                              .stream()
                              .flatMap(entry ->
                                               entry.getValue().stream()
                                                    .map(date -> ExchangeRateItem.builder()
                                                                                 .date(date)
                                                                                 .pair(entry.getKey())
                                                                                 .build()
                                                    )
                              )
                              .toList();

        List<ExchangeRateItemResponse> foundRates = exchangeService.getAllRatesForEachPairByDate(requestedItems);

        Set<ExchangeRateItem> foundItems = foundRates.stream()
                                                     .map(ExchangeRateItemResponse::getItem)
                                                     .collect(Collectors.toSet());

        List<ExchangeRateItem> missingItems = requestedItems.stream()
                                                            .filter(item -> !foundItems.contains(item))
                                                            .toList();

        List<ExchangeRateItemResponse> ratesFromClient = getMissingRatesFromClient(missingItems);

        List<ExchangeRateItemResponse> allRates = new ArrayList<>(foundRates);
        allRates.addAll(ratesFromClient);

        Map<CurrencyPair, Map<LocalDate, BigDecimal>> groupedRates = new HashMap<>();

        for (ExchangeRateItemResponse response : allRates) {
            CurrencyPair pair = response.getItem().getPair();
            LocalDate date = response.getItem().getDate();
            BigDecimal rate = response.getRate();

            Map<LocalDate, BigDecimal> innerMap =
                    groupedRates.computeIfAbsent(pair, k -> new HashMap<>());
            innerMap.put(date, rate);
        }

        return groupedRates.entrySet().stream()
                           .map(entry -> ExchangeRateResponse.builder()
                                                             .pair(entry.getKey())
                                                             .rates(entry.getValue())
                                                             .build())
                           .toList();
    }

    private List<ExchangeRateItemResponse> getMissingRatesFromClient(List<ExchangeRateItem> missingItems) {

        Map<CurrencyPair, List<ExchangeRateItem>> groupedByPair =
                missingItems.stream()
                            .collect(Collectors.groupingBy(
                                    ExchangeRateItem::getPair));

        List<ExchangeRateItemResponse> result = new ArrayList<>();

        for (Map.Entry<CurrencyPair, List<ExchangeRateItem>> entry : groupedByPair.entrySet()) {
            CurrencyPair pair = entry.getKey();
            List<ExchangeRateItem> itemsForPair = entry.getValue();

            String symbol = currencyPairUtil.convertToTwelveDataPair(pair);

            int calcOutputSize = (int) getOutputSizeFromToday(itemsForPair);

            CurrencyResponseDto response = currencyClient.getRate(
                    symbol,
                    currencyClientProperty.getInterval(),
                    Math.max(calcOutputSize, currencyClientProperty.getOutputSize()),
                    currencyClientProperty.isPreviousClose(),
                    currencyClientProperty.getApiKey()
            );


            validateResponse(response);

            Map<LocalDate, BigDecimal> ratesByDate =
                    response.getValues().stream()
                            .collect(Collectors.toMap(
                                    CurrencyResponseDto.CurrencyRate::getRateDate,
                                    rate ->
                                            rate.getClose() != null ? rate.getClose() : rate.getPreviousClose()
                            ));

            for (ExchangeRateItem item : itemsForPair) {
                BigDecimal rate = ratesByDate.get(item.getDate());
                if (rate != null) {
                    result.add(ExchangeRateItemResponse.builder()
                                                       .item(item)
                                                       .rate(rate)
                                                       .build());
                }
            }
        }

        return result;
    }

    private void validateResponse(CurrencyResponseDto response) {
        Set<ConstraintViolation<CurrencyResponseDto>> violations = validator.validate(response);

        if (!violations.isEmpty()) {
            List<String> errorMessages =
                    violations.stream()
                              .map(ConstraintViolation::getMessage)
                              .collect(Collectors.toList());

            throw new InvalidCurrencyResponseException(errorMessages);
        }
    }

    private long getOutputSizeFromToday(List<ExchangeRateItem> itemsForPair) {

        LocalDate earliestDate = itemsForPair.stream()
                                             .map(ExchangeRateItem::getDate)
                                             .min(LocalDate::compareTo)
                                             .orElseThrow();

        long daysBack = ChronoUnit.DAYS.between(earliestDate, LocalDate.now());

        return daysBack + 1;
    }
}
