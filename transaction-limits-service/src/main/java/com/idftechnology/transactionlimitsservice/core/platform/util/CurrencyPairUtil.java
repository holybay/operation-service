package com.idftechnology.transactionlimitsservice.core.platform.util;

import com.idftechnology.transactionlimitsservice.core.platform.exception.CurrencyPairIllegalStateException;
import com.idftechnology.transactionlimitsservice.core.service.dto.CurrencyPair;

import java.util.Optional;

public class CurrencyPairUtil {

    public String convertToTwelveDataPair(CurrencyPair pair) {
        return Optional.of(pair)
                       .map(p -> String.join("/", pair.getFrom(), pair.getTo()))
                       .orElseThrow(() -> new CurrencyPairIllegalStateException(
                               "Wasn't able to parse currency pair\n " + pair
                       ));
    }

}
