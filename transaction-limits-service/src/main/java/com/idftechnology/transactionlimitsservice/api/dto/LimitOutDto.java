package com.idftechnology.transactionlimitsservice.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.idftechnology.transactionlimitsservice.core.platform.util.jackson.CustomCurrencySerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Currency;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LimitOutDto {

    @JsonProperty("limit_datetime")
    private OffsetDateTime dateFrom;

    @JsonProperty("limit_sum")
    private BigDecimal sum;

    @JsonProperty("limit_currency_shortname")
    @JsonSerialize(using = CustomCurrencySerializer.class)
    private Currency currency;

    @JsonProperty("expense_category")
    private String expenseCategory;
}
