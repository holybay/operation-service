package com.idftechnology.transactionlimitsservice.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.idftechnology.transactionlimitsservice.core.platform.validation.api.CurrencyValid;
import com.idftechnology.transactionlimitsservice.core.platform.validation.api.ExpenseCategoryValid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.ZoneOffset;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LimitCreateDto {

    @NotNull
    private ZoneOffset zone;

    @Positive
    @NotNull
    private BigDecimal sum;

    @JsonProperty("currency_shortname")
    @CurrencyValid
    private String currency;

    @JsonProperty("expense_category")
    @ExpenseCategoryValid
    private String expenseCategory;

}
