package com.idftechnology.transactionlimitsservice.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.idftechnology.transactionlimitsservice.core.platform.validation.api.CurrencyValid;
import com.idftechnology.transactionlimitsservice.core.platform.validation.api.ExpenseCategoryValid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionCreateDto {

    @JsonProperty("account_from")
    @NotNull
    private Long accountFrom;

    @JsonProperty("account_to")
    @NotNull
    private Long accountTo;

    @JsonProperty("currency_shortname")
    @CurrencyValid
    private String currency;

    @NotNull
    private BigDecimal sum;

    @JsonProperty("expense_category")
    @ExpenseCategoryValid
    private String expenseCategory;

    @JsonProperty("datetime")
    @FutureOrPresent
    private OffsetDateTime dateTime;

}
