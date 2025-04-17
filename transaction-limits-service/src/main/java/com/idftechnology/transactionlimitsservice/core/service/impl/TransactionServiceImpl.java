package com.idftechnology.transactionlimitsservice.core.service.impl;

import com.idftechnology.transactionlimitsservice.api.dto.TransactionCreateDto;
import com.idftechnology.transactionlimitsservice.api.dto.TransactionOutDto;
import com.idftechnology.transactionlimitsservice.core.platform.mapper.TransactionMapper;
import com.idftechnology.transactionlimitsservice.core.platform.util.DateTimeUtil;
import com.idftechnology.transactionlimitsservice.core.repository.api.TransactionRepository;
import com.idftechnology.transactionlimitsservice.core.repository.entity.Limit;
import com.idftechnology.transactionlimitsservice.core.repository.entity.Transaction;
import com.idftechnology.transactionlimitsservice.core.service.api.ExchangeRateFacade;
import com.idftechnology.transactionlimitsservice.core.service.api.LimitInitializeService;
import com.idftechnology.transactionlimitsservice.core.service.api.LimitService;
import com.idftechnology.transactionlimitsservice.core.service.api.TransactionService;
import com.idftechnology.transactionlimitsservice.core.service.dto.CurrencyPair;
import com.idftechnology.transactionlimitsservice.core.service.dto.ExchangeRateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository repository;
    private final TransactionMapper mapper;
    private final LimitService limitService;
    private final DateTimeUtil dateTimeUtil;
    private final ExchangeRateFacade exchangeRateFacade;
    private final LimitInitializeService limitInitService;

    @Transactional
    @Override
    public TransactionOutDto create(TransactionCreateDto dto) {

        limitInitService.checkAndInit(dto.getAccountFrom(), dto.getDateTime().getOffset());

        List<Transaction> allInThisMonth = getAllTransactionsForCurrentMonth(dto);

        Map<CurrencyPair, List<LocalDate>> allDatesByPair = new HashMap<>();
        Map<CurrencyPair, List<Transaction>> allTransactionsByPairs = new HashMap<>();

        processNonUsdTransactions(dto, allInThisMonth, allDatesByPair, allTransactionsByPairs);

        List<ExchangeRateResponse> allRates = exchangeRateFacade.getRatesByPairForDates(allDatesByPair);

        Map<CurrencyPair, Map<LocalDate, BigDecimal>> ratesGroupByPair =
                allRates.stream()
                        .collect(Collectors.toMap(ExchangeRateResponse::getPair,
                                                  ExchangeRateResponse::getRates));


        BigDecimal totalSumInUsdForNonUsdTransactions = calculateTotalNonUsdTransactionSumToUsd(allTransactionsByPairs,
                                                                                                ratesGroupByPair);
        BigDecimal totalSumForUsdTransactions = calculateTotalForUsd(allInThisMonth);


        if (isUsd(dto.getCurrency())) {
            totalSumForUsdTransactions = totalSumForUsdTransactions.add(dto.getSum());
        } else {
            CurrencyPair currentTransactionPair = getUsdToCurrencyPair(dto.getCurrency());
            BigDecimal rateForToday = ratesGroupByPair.get(currentTransactionPair).get(dto.getDateTime().toLocalDate());

            totalSumInUsdForNonUsdTransactions = totalSumInUsdForNonUsdTransactions.add(
                    dto.getSum().divide(rateForToday, 2, RoundingMode.HALF_UP));
        }

        BigDecimal totalInUsdWithCurrent = totalSumForUsdTransactions.add(totalSumInUsdForNonUsdTransactions);

        Limit limitToCompare = limitService.getByAccountIdAndExpenseCategory(dto.getAccountFrom(),
                                                                             dto.getExpenseCategory());

        boolean limitExceeded = totalInUsdWithCurrent.compareTo(limitToCompare.getSum()) > 0;

        Transaction entity = mapper.toEntity(dto, limitExceeded);
        repository.saveAndFlush(entity);
        return mapper.toDto(entity, limitToCompare);
    }

    @Transactional(readOnly = true)
    @Override
    public List<TransactionOutDto> getAll(Long accountId, String expenseCategory, ZoneOffset zone, Pageable pageable) {
        return repository.findAllByAccountFrom(accountId, expenseCategory,
                                               dateTimeUtil.convertToMonthStart(OffsetDateTime.now(zone)),
                                               dateTimeUtil.convertToMonthEnd(OffsetDateTime.now(zone)), pageable
                         ).stream()
                         .map(t -> {
                             Limit limit = limitService.getByAccountIdAndExpenseCategory(accountId,
                                                                                         t.getExpenseCategory().getName());
                             return mapper.toDto(t, limit);
                         })
                         .toList();
    }

    private boolean isUsd(String currencyToCheck) {
        return Objects.equals("USD", currencyToCheck);
    }

    private void processNonUsdTransactions(TransactionCreateDto dto,
                                           List<Transaction> allInThisMonth,
                                           Map<CurrencyPair, List<LocalDate>> allDatesByPair,
                                           Map<CurrencyPair, List<Transaction>> allTransactionsByPairs) {

        allInThisMonth.stream()
                      .filter(t -> !isUsd(t.getCurrency().getCurrencyCode()))
                      .forEach(t -> {
                          groupTransactionDatesByCurrencyPair(t.getCurrency(), t.getDateTime(), allDatesByPair);
                          groupTransactionsByCurrencyPair(t, allTransactionsByPairs);
                      });
        if (!isUsd(dto.getCurrency())) {
            Currency currency = Currency.getInstance(dto.getCurrency());
            groupTransactionDatesByCurrencyPair(currency, dto.getDateTime(), allDatesByPair);
        }
    }

    private BigDecimal calculateTotalNonUsdTransactionSumToUsd(
            Map<CurrencyPair, List<Transaction>> allTransactionsByPairs,
            Map<CurrencyPair, Map<LocalDate, BigDecimal>> pairRatesByDate) {

        return allTransactionsByPairs.entrySet().stream()
                                     .flatMap(entry -> {
                                         CurrencyPair pair = entry.getKey();
                                         List<Transaction> transactions = entry.getValue();
                                         Map<LocalDate, BigDecimal> ratesByDate = pairRatesByDate.get(pair);

                                         return convertAllPairTransactionToUsd(transactions, ratesByDate);
                                     })
                                     .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateTotalForUsd(List<Transaction> allInThisMonth) {
        return allInThisMonth.stream()
                             .filter(t -> isUsd(t.getCurrency().getCurrencyCode()))
                             .map(Transaction::getSum)
                             .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Stream<BigDecimal> convertAllPairTransactionToUsd(List<Transaction> transactions,
                                                              Map<LocalDate, BigDecimal> ratesByDate) {
        return transactions.stream()
                           .map(t -> {
                               LocalDate date = t.getDateTime().toLocalDate();
                               BigDecimal rate = ratesByDate.get(date);
                               return t.getSum().divide(rate, 2,
                                                        RoundingMode.HALF_UP);
                           });
    }

    private List<Transaction> getAllTransactionsForCurrentMonth(TransactionCreateDto dto) {
        return repository.findAllByLimitBetween(
                dto.getAccountFrom(),
                dto.getExpenseCategory(),
                dateTimeUtil.convertToMonthStart(dto.getDateTime()),
                dateTimeUtil.convertToMonthEnd(dto.getDateTime())
        );
    }

    private void groupTransactionDatesByCurrencyPair(Currency currencyTo, OffsetDateTime transactionDate,
                                                     Map<CurrencyPair, List<LocalDate>> allDatesByPair) {

        CurrencyPair pair = getUsdToCurrencyPair(currencyTo.getCurrencyCode());
        allDatesByPair.compute(pair, computeListAdder(transactionDate.toLocalDate()));
    }


    private void groupTransactionsByCurrencyPair(Transaction t,
                                                 Map<CurrencyPair, List<Transaction>> allTransactionsByPairs) {

        CurrencyPair pair = getUsdToCurrencyPair(t.getCurrency().getCurrencyCode());
        allTransactionsByPairs.compute(pair, computeListAdder(t));
    }

    private CurrencyPair getUsdToCurrencyPair(String currencyTo) {
        return CurrencyPair.builder()
                           .from("USD")
                           .to(currencyTo)
                           .build();
    }

    private <K, V> BiFunction<K, List<V>, List<V>> computeListAdder(V element) {
        return (key, list) -> {
            if (list == null) {
                list = new ArrayList<>();
            }
            list.add(element);
            return list;
        };
    }

}
