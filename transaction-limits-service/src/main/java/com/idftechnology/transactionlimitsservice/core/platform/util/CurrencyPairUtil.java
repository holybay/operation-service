package com.idftechnology.transactionlimitsservice.core.platform.util;

import com.idftechnology.transactionlimitsservice.core.platform.exception.CurrencyPairIllegalStateException;
import com.idftechnology.transactionlimitsservice.core.service.dto.CurrencyPair;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CurrencyPairUtil {

    public String convertToTwelveDataPair(@NotNull CurrencyPair pair) {
        return Optional.of(pair)
                       .map(p -> String.join("/", pair.getFrom(), pair.getTo()))
                       .orElseThrow(() -> new CurrencyPairIllegalStateException(
                               "Wasn't able to parse currency pair\n " + pair
                       ));
    }

}
