package com.idftechnology.transactionlimitsservice.core.service.scheduler;

import com.idftechnology.transactionlimitsservice.core.config.property.TwelveDataProperty;
import com.idftechnology.transactionlimitsservice.core.platform.exception.CurrencyRateNonValidException;
import com.idftechnology.transactionlimitsservice.core.platform.util.CurrencyPairUtil;
import com.idftechnology.transactionlimitsservice.core.service.api.ExchangeRateService;
import com.idftechnology.transactionlimitsservice.core.service.dto.CurrencyPair;
import com.idftechnology.transactionlimitsservice.core.service.dto.CurrencyResponseDto;
import com.idftechnology.transactionlimitsservice.core.service.dto.ExchangeRateItem;
import com.idftechnology.transactionlimitsservice.core.service.feign.client.TwelveDataClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Component
@RequiredArgsConstructor
@Log4j2
public class ExchangeRateScheduler {

    private static final List<CurrencyPair> MOST_USING_PAIRS = List.of(
            new CurrencyPair("USD", "KZT"),
            new CurrencyPair("USD", "RUB")
    );
    private final TwelveDataClient twelveDataClient;
    private final ExchangeRateService exchangeRateService;
    private final TwelveDataProperty twelveDataProperty;
    private final CurrencyPairUtil currencyPairUtil;

    @Scheduled(cron = "0 0 2 * * *")
    public void receiveAndSaveDailyRates() {
        log.info("Running exchange rate updating to Database");

        for (CurrencyPair pair : MOST_USING_PAIRS) {
            try {
                processPair(pair);
            } catch (Exception ex) {
                log.error("Failed to process pair {}: {}", pair, ex.getMessage(), ex);
            }
        }
        log.info("Daily rates updates finished.");
    }

    private void processPair(CurrencyPair pair) {
        String symbol = currencyPairUtil.convertToTwelveDataPair(pair);

        CurrencyResponseDto response = twelveDataClient.getRate(
                symbol,
                twelveDataProperty.getInterval(),
                twelveDataProperty.getOutputSize(),
                twelveDataProperty.isPreviousClose(),
                twelveDataProperty.getApiKey()
        );

        List<CurrencyResponseDto.CurrencyRate> currencyRates = response.getValues();

        if (currencyRates == null || currencyRates.isEmpty()) {
            throw new CurrencyRateNonValidException(
                    String.format("No rates for pair {%s} on %s. UTC: %s",
                                  pair, LocalDate.now(), OffsetDateTime.now(ZoneOffset.UTC))
            );
        }

        CurrencyResponseDto.CurrencyRate currencyRate = currencyRates.get(0);
        BigDecimal close = currencyRate.getClose();
        BigDecimal previousClose = currencyRate.getPreviousClose();

        ExchangeRateItem item = ExchangeRateItem.builder()
                                                .pair(pair)
                                                .date(LocalDate.now())
                                                .build();

        exchangeRateService.save(item, close, previousClose);

        log.info("Saved rate for {}: close={}, previousClose={}", symbol, close, previousClose);
    }

}
