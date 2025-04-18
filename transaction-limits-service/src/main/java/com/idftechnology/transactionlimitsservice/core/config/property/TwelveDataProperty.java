package com.idftechnology.transactionlimitsservice.core.config.property;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@ConfigurationProperties(prefix = "clients.currency.twelve-data.property")
@Validated
public class TwelveDataProperty {

    @NotBlank
    private String apiKey;

    @NotBlank
    private String interval;

    @Positive
    private int outputSize;

    private boolean previousClose;

}
