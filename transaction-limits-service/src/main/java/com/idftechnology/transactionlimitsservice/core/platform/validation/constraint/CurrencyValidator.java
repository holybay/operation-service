package com.idftechnology.transactionlimitsservice.core.platform.validation.constraint;

import com.idftechnology.transactionlimitsservice.core.platform.validation.api.CurrencyValid;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Currency;

public class CurrencyValidator implements ConstraintValidator<CurrencyValid, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            Currency.getInstance(value.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
