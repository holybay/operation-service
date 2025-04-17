package com.idftechnology.transactionlimitsservice.core.platform.util.mapper;

import com.idftechnology.transactionlimitsservice.core.repository.entity.ExpenseCategory;
import com.idftechnology.transactionlimitsservice.core.service.api.ExpenseCategoryService;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.Currency;

@Component
@RequiredArgsConstructor
public class BaseMapperHelper {

    private final ExpenseCategoryService categoryService;

    @Named("mapToCurrency")
    public Currency mapToCurrency(String name) {
        return Currency.getInstance(name.toUpperCase());
    }

    @Named("getZoneOffset")
    public String getZoneOffset(OffsetDateTime offsetDateTime) {
        return offsetDateTime.getOffset().getId();
    }

    @Named("mapCategoryByName")
    public ExpenseCategory mapCategoryByName(String name) {
        return categoryService.getByName(name);
    }
}
