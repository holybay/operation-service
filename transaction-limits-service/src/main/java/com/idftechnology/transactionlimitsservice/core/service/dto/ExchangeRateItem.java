package com.idftechnology.transactionlimitsservice.core.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateItem {

    private LocalDate date;

    private CurrencyPair pair;

}
