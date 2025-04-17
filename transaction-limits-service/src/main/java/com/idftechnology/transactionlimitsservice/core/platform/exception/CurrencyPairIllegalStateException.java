package com.idftechnology.transactionlimitsservice.core.platform.exception;

public class CurrencyPairIllegalStateException extends IllegalStateException {

    public CurrencyPairIllegalStateException() {
        super();
    }

    public CurrencyPairIllegalStateException(Throwable cause) {
        super(cause);
    }

    public CurrencyPairIllegalStateException(String message) {
        super(message);
    }

    public CurrencyPairIllegalStateException(String message, Throwable cause) {
        super(message, cause);
    }
}
