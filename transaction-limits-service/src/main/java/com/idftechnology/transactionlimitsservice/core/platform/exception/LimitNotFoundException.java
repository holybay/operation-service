package com.idftechnology.transactionlimitsservice.core.platform.exception;

import java.util.NoSuchElementException;

public class LimitNotFoundException extends NoSuchElementException {

    public LimitNotFoundException() {
        super();
    }

    public LimitNotFoundException(Throwable cause) {
        super(cause);
    }

    public LimitNotFoundException(String message) {
        super(message);
    }

    public LimitNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
