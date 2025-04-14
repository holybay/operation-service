package com.idftechnology.transactionlimitsservice.core.platform.util.mapper;

import com.idftechnology.transactionlimitsservice.core.repository.entity.ExpenseCategory;
import com.idftechnology.transactionlimitsservice.core.service.api.ExpenseCategoryService;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Component
@RequiredArgsConstructor
public class LimitMapperHelper {

    private final ExpenseCategoryService categoryService;

    @Named("newLimitStart")
    public OffsetDateTime setLimitDateFrom(ZoneOffset offset) {
        return OffsetDateTime.now(offset);
    }

    @Named("mapCategoryByName")
    public ExpenseCategory mapCategoryByName(String name) {
        return categoryService.getByName(name);
    }
}
