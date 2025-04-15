package com.idftechnology.transactionlimitsservice.core.platform.mapper;

import com.idftechnology.transactionlimitsservice.api.dto.LimitCreateDto;
import com.idftechnology.transactionlimitsservice.api.dto.LimitOutDto;
import com.idftechnology.transactionlimitsservice.core.platform.util.mapper.BaseMapperHelper;
import com.idftechnology.transactionlimitsservice.core.platform.util.mapper.LimitMapperHelper;
import com.idftechnology.transactionlimitsservice.core.repository.entity.ExpenseCategory;
import com.idftechnology.transactionlimitsservice.core.repository.entity.Limit;
import com.idftechnology.transactionlimitsservice.core.service.api.ExpenseCategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {LimitMapperImpl.class, ExpenseCategoryService.class,
        BaseMapperHelper.class, LimitMapperHelper.class})
class LimitMapperTest {

    @Autowired
    private LimitMapper mapper;

    @MockitoBean
    private ExpenseCategoryService categoryService;

    @BeforeEach
    void init() {
        when(categoryService.getByName("product"))
                .thenReturn(ExpenseCategory.builder().id(1L).name("product").build());
    }

    @Test
    void toDto() {

        Limit given = Limit.builder()
                           .accountFrom(123L)
                           .dateFrom(OffsetDateTime.now())
                           .sum(BigDecimal.valueOf(2000))
                           .currency(Currency.getInstance("USD"))
                           .expenseCategory(ExpenseCategory.builder().id(1L).name("product").build())
                           .build();

        LimitOutDto actualResult = mapper.toDto(given);

        assertNotNull(actualResult.getDateFrom());
        assertEquals(0, given.getSum().compareTo(actualResult.getSum()));
        assertEquals(actualResult.getCurrency(), given.getCurrency());
        assertEquals(actualResult.getExpenseCategory(), given.getExpenseCategory().getName());

    }

    @Test
    void toEntity() {
        LimitCreateDto given = LimitCreateDto.builder()
                                             .sum(BigDecimal.valueOf(2000))
                                             .currency("USD")
                                             .expenseCategory("product")
                                             .build();

        Limit actualResult = mapper.toEntity(given, 123L, ZoneOffset.ofHours(3));

        assertNotNull(actualResult.getAccountFrom());
        assertEquals(ZoneOffset.ofHours(3), actualResult.getDateFrom().getOffset());
        assertEquals(0, given.getSum().compareTo(actualResult.getSum()));
        assertEquals(given.getCurrency(), actualResult.getCurrency().getCurrencyCode());
        assertEquals(given.getExpenseCategory(), actualResult.getExpenseCategory().getName());

        verify(categoryService).getByName(given.getExpenseCategory());
    }

}