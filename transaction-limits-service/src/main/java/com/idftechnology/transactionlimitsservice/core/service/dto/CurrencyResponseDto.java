package com.idftechnology.transactionlimitsservice.core.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.idftechnology.transactionlimitsservice.core.platform.util.jackson.CustomTwelveDataLocalDateDeserializer;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyResponseDto {

    List<@Valid CurrencyRate> values;

    @Valid
    private Meta meta;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    private static class Meta {

        @NotBlank
        private String symbol;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    private static class CurrencyRate {

        @JsonProperty("datetime")
        @JsonDeserialize(using = CustomTwelveDataLocalDateDeserializer.class)
        @NotNull
        private LocalDate rateDate;

        private BigDecimal close;

        @JsonProperty("previous_close")
        private BigDecimal previousClose;

    }
}
