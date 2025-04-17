package com.idftechnology.transactionlimitsservice.core.platform.exception;

public class CurrencyRateNonValidException extends RuntimeException {

    public CurrencyRateNonValidException() {
        super();
    }

    public CurrencyRateNonValidException(Throwable cause) {
        super(cause);
    }

    public CurrencyRateNonValidException(String message) {
        super(message);
    }

    public CurrencyRateNonValidException(String message, Throwable cause) {
        super(message, cause);
    }
}
