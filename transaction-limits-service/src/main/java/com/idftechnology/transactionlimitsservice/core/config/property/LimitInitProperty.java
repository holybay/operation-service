package com.idftechnology.transactionlimitsservice.core.config.property;

import com.idftechnology.transactionlimitsservice.core.platform.validation.api.ExpenseCategoryValid;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.Map;

@Data
@ConfigurationProperties(prefix = "app.property")
@Validated
public class LimitInitProperty {

    private Map<@ExpenseCategoryValid String, BigDecimal> limitsByDefault;
}
