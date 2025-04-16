package com.idftechnology.transactionlimitsservice.core.platform.exception;

import java.util.List;

public class InvalidCurrencyResponseException extends RuntimeException {

    private final List<String> errorMessages;

    public InvalidCurrencyResponseException(List<String> errorMessages) {
        super("Currency response is invalid");
        this.errorMessages = errorMessages;
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }
}
