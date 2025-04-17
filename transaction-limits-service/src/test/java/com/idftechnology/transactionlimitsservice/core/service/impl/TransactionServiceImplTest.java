package com.idftechnology.transactionlimitsservice.core.service.impl;

import com.idftechnology.transactionlimitsservice.api.dto.TransactionCreateDto;
import com.idftechnology.transactionlimitsservice.api.dto.TransactionOutDto;
import com.idftechnology.transactionlimitsservice.core.platform.mapper.TransactionMapper;
import com.idftechnology.transactionlimitsservice.core.platform.util.DateTimeUtil;
import com.idftechnology.transactionlimitsservice.core.repository.api.TransactionRepository;
import com.idftechnology.transactionlimitsservice.core.repository.entity.ExpenseCategory;
import com.idftechnology.transactionlimitsservice.core.repository.entity.Limit;
import com.idftechnology.transactionlimitsservice.core.repository.entity.Transaction;
import com.idftechnology.transactionlimitsservice.core.service.api.ExchangeRateFacade;
import com.idftechnology.transactionlimitsservice.core.service.api.LimitInitializeService;
import com.idftechnology.transactionlimitsservice.core.service.api.LimitService;
import com.idftechnology.transactionlimitsservice.core.service.dto.CurrencyPair;
import com.idftechnology.transactionlimitsservice.core.service.dto.ExchangeRateResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    private final BigDecimal usdToRubRate = BigDecimal.valueOf(80.00000);

    private final BigDecimal usdToKztRate = BigDecimal.valueOf(550.00000);

    @Mock
    private TransactionRepository repository;

    @Mock
    private LimitService limitService;

    @Mock
    private DateTimeUtil dateTimeUtil;

    @Mock
    private ExchangeRateFacade exchangeRateFacade;

    @Mock
    private TransactionMapper mapper;

    @Mock
    private LimitInitializeService limitInitService;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Captor
    private ArgumentCaptor<Transaction> transactionCaptor;

    private Long accountId;

    private ExpenseCategory category;

    private ZoneOffset zone;
    private LocalDate transactionDate;


    @BeforeEach
    void setUp() {
        accountId = 1L;
        zone = ZoneOffset.UTC;
        category = ExpenseCategory.builder()
                                  .id(1L)
                                  .name("product")
                                  .build();

        transactionDate = LocalDate.of(2025, 1, 15);
        when(dateTimeUtil.convertToMonthStart(any())).
                thenReturn(OffsetDateTime.of(LocalDate.of(2025, 1, 1), LocalTime.MIN, zone));

        when(dateTimeUtil.convertToMonthEnd(any()))
                .thenReturn(OffsetDateTime.of(LocalDate.of(2025, 1, 1), LocalTime.MAX, zone));

        doNothing().when(limitInitService).checkAndInit(anyLong(), any());
    }

    @Test
    void shouldReturnLimitExceededTrue() {

        TransactionCreateDto createDto = getTransactionCreateDto(transactionDate,
                                                                 BigDecimal.valueOf(16_000),
                                                                 "RUB");

        List<Transaction> existingTransactions =
                Stream.of(getTransactionEntity(LocalDate.of(2025, 1, 2),
                                               BigDecimal.valueOf(500),
                                               "USD",
                                               false),
                          getTransactionEntity(LocalDate.of(2025, 1, 10),
                                               BigDecimal.valueOf(400),
                                               "USD",
                                               false)
                ).toList();

        Map<CurrencyPair, List<LocalDate>> allDatesByPair = new HashMap<>();
        CurrencyPair newPair = CurrencyPair.builder()
                                           .from("USD")
                                           .to(createDto.getCurrency())
                                           .build();
        allDatesByPair.put(newPair, List.of(createDto.getDateTime().toLocalDate()));

        when(exchangeRateFacade.getRatesByPairForDates(allDatesByPair))
                .thenReturn(List.of(ExchangeRateResponse.builder()
                                                        .pair(newPair)
                                                        .rates(Map.of(createDto.getDateTime().toLocalDate(),
                                                                      usdToRubRate))
                                                        .build()
                ));

        when(repository.findAllByLimitBetween(eq(accountId), eq(category.getName()), any(), any()))
                .thenReturn(existingTransactions);

        Limit currentLimit = generateLimitEntity(LocalDate.of(2025, 1, 1),
                                                 BigDecimal.valueOf(1000));

        when(limitService.getByAccountIdAndExpenseCategory(accountId, category.getName()))
                .thenReturn(currentLimit);

        Transaction savedEntity = getTransactionEntity(createDto.getDateTime().toLocalDate(),
                                                       createDto.getSum(),
                                                       createDto.getCurrency(),
                                                       true);

        when(mapper.toEntity(any(TransactionCreateDto.class), anyBoolean()))
                .thenReturn(savedEntity);

        TransactionOutDto expectedOutDto = getTransactionOutDto(savedEntity, currentLimit);

        when(mapper.toDto(savedEntity, currentLimit))
                .thenReturn(expectedOutDto);


        TransactionOutDto actualResult = transactionService.create(createDto);

        verify(limitInitService).checkAndInit(anyLong(), any());
        verify(repository).findAllByLimitBetween(any(), any(), any(), any());
        verify(dateTimeUtil).convertToMonthStart(any(OffsetDateTime.class));
        verify(dateTimeUtil).convertToMonthEnd(any(OffsetDateTime.class));
        verify(exchangeRateFacade).getRatesByPairForDates(any());
        verify(limitService).getByAccountIdAndExpenseCategory(any(), any());
        verify(mapper).toEntity(eq(createDto), anyBoolean());
        verify(repository).saveAndFlush(transactionCaptor.capture());
        verify(mapper).toDto(any(), any());

        assertEquals(expectedOutDto, actualResult);
        assertTrue(transactionCaptor.getValue().isLimitExceeded());
    }

    @Test
    void shouldReturnLimitExceededFalse() {
        TransactionCreateDto createDto = getTransactionCreateDto(transactionDate,
                                                                 BigDecimal.valueOf(55_000),
                                                                 "KZT");

        List<Transaction> existingTransactions =
                Stream.of(getTransactionEntity(LocalDate.of(2025, 1, 2),
                                               BigDecimal.valueOf(500),
                                               "USD",
                                               false),
                          getTransactionEntity(LocalDate.of(2025, 1, 10),
                                               BigDecimal.valueOf(300),
                                               "USD",
                                               false)
                ).toList();

        Map<CurrencyPair, List<LocalDate>> allDatesByPair = new HashMap<>();
        CurrencyPair newPair = CurrencyPair.builder()
                                           .from("USD")
                                           .to(createDto.getCurrency())
                                           .build();
        allDatesByPair.put(newPair, List.of(createDto.getDateTime().toLocalDate()));

        when(exchangeRateFacade.getRatesByPairForDates(allDatesByPair))
                .thenReturn(List.of(ExchangeRateResponse.builder()
                                                        .pair(newPair)
                                                        .rates(Map.of(createDto.getDateTime().toLocalDate(),
                                                                      usdToKztRate))
                                                        .build()
                ));

        when(repository.findAllByLimitBetween(eq(accountId), eq(category.getName()), any(), any()))
                .thenReturn(existingTransactions);

        Limit currentLimit = generateLimitEntity(LocalDate.of(2025, 1, 1),
                                                 BigDecimal.valueOf(1000));

        when(limitService.getByAccountIdAndExpenseCategory(accountId, category.getName()))
                .thenReturn(currentLimit);

        Transaction savedEntity = getTransactionEntity(createDto.getDateTime().toLocalDate(),
                                                       createDto.getSum(),
                                                       createDto.getCurrency(),
                                                       false);

        when(mapper.toEntity(any(TransactionCreateDto.class), anyBoolean()))
                .thenReturn(savedEntity);

        TransactionOutDto expectedOutDto = getTransactionOutDto(savedEntity, currentLimit);

        when(mapper.toDto(savedEntity, currentLimit))
                .thenReturn(expectedOutDto);


        TransactionOutDto actualResult = transactionService.create(createDto);

        verify(repository).findAllByLimitBetween(any(), any(), any(), any());
        verify(dateTimeUtil).convertToMonthStart(any(OffsetDateTime.class));
        verify(dateTimeUtil).convertToMonthEnd(any(OffsetDateTime.class));
        verify(exchangeRateFacade).getRatesByPairForDates(any());
        verify(limitService).getByAccountIdAndExpenseCategory(any(), any());
        verify(mapper).toEntity(eq(createDto), anyBoolean());
        verify(repository).saveAndFlush(transactionCaptor.capture());
        verify(mapper).toDto(any(), any());

        assertEquals(expectedOutDto, actualResult);
        assertFalse(transactionCaptor.getValue().isLimitExceeded());
    }

    private Limit generateLimitEntity(LocalDate date, BigDecimal sum) {
        return Limit.builder()
                    .dateFrom(setOffsetDate(date))
                    .sum(sum)
                    .expenseCategory(category)
                    .accountFrom(accountId)
                    .build();
    }

    private TransactionCreateDto getTransactionCreateDto(LocalDate date, BigDecimal sum, String currency) {
        return TransactionCreateDto.builder()
                                   .accountFrom(accountId)
                                   .expenseCategory(category.getName())
                                   .currency(currency)
                                   .dateTime(setOffsetDate(date))
                                   .sum(sum)
                                   .build();
    }

    private Transaction getTransactionEntity(LocalDate date, BigDecimal sum, String currency, boolean limitExceeded) {
        return Transaction.builder()
                          .accountFrom(accountId)
                          .currency(Currency.getInstance(currency))
                          .dateTime(setOffsetDate(date))
                          .zone(zone.getId())
                          .sum(sum)
                          .expenseCategory(category)
                          .limitExceeded(limitExceeded)
                          .build();
    }

    private TransactionOutDto getTransactionOutDto(Transaction t, Limit l) {
        return TransactionOutDto.builder()
                                .accountFrom(t.getAccountFrom())
                                .currency(t.getCurrency())
                                .sum(t.getSum())
                                .expenseCategory(t.getExpenseCategory().getName())
                                .dateTime(t.getDateTime())
                                .limitSum(l.getSum())
                                .limitDateFrom(l.getDateFrom())
                                .limitCurrency(l.getCurrency())
                                .build();
    }

    private OffsetDateTime setOffsetDate(LocalDate date) {
        return OffsetDateTime.of(date, LocalTime.MIN, zone);
    }
}