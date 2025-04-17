package com.idftechnology.transactionlimitsservice.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.idftechnology.transactionlimitsservice.core.platform.util.jackson.CustomCurrencySerializer;
import io.swagger.v3.oas.annotations.media.Schema;
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
public class TransactionOutDto {

    @JsonProperty("account_from")
    private Long accountFrom;

    @JsonProperty("account_to")
    private Long accountTo;

    @JsonProperty("currency_shortname")
    @JsonSerialize(using = CustomCurrencySerializer.class)
    private Currency currency;

    private BigDecimal sum;

    @JsonProperty("expense_category")
    private String expenseCategory;

    @Schema(example = "2025-04-17 08:22:22Z", description = "Дата создания транзакции")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ssX", timezone = "UTC")
    @JsonProperty("datetime")
    private OffsetDateTime dateTime;

    @JsonProperty("limit_sum")
    private BigDecimal limitSum;

    @Schema(example = "2025-04-17 08:22:22Z", description = "Дата и время когда был установлен лимит")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ssX", timezone = "UTC")
    @JsonProperty("limit_datetime")
    private OffsetDateTime limitDateFrom;

    @JsonProperty("limit_currency_shortname")
    @JsonSerialize(using = CustomCurrencySerializer.class)
    private Currency limitCurrency;

}
