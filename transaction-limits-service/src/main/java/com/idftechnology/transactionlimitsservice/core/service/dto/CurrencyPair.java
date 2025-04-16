package com.idftechnology.transactionlimitsservice.core.service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CurrencyPair {

    private String from;

    private String to;
}
