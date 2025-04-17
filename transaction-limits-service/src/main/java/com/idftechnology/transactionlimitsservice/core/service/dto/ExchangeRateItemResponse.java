package com.idftechnology.transactionlimitsservice.core.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateItemResponse {

    private ExchangeRateItem item;

    private BigDecimal rate;

}
