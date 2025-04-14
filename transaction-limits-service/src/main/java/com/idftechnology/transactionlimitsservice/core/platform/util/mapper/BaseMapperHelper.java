package com.idftechnology.transactionlimitsservice.core.platform.util.mapper;

import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.Currency;

@Component
public class BaseMapperHelper {

    @Named("mapToCurrency")
    public Currency mapToCurrency(String name) {
        return Currency.getInstance(name.toUpperCase());
    }
}
