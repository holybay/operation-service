package com.idftechnology.transactionlimitsservice.api.controller.advice;

import com.idftechnology.transactionlimitsservice.core.platform.exception.InvalidCurrencyResponseException;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestControllerAdvice
@Log4j2
public class GlobalHandler {

    private static final String BAD_REQUEST_MESSAGE = "Something went wrong";

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handle(Exception e) {
        log.error("Internal server error: {}", e.getMessage(), e);
        return BAD_REQUEST_MESSAGE;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handle(NoSuchElementException e) {
        log.error(e.getMessage(), e);
        return BAD_REQUEST_MESSAGE;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handle(IllegalArgumentException e) {
        log.error(e.getMessage(), e);
        return BAD_REQUEST_MESSAGE;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handle(IllegalStateException e) {
        log.error(e.getMessage(), e);
        return BAD_REQUEST_MESSAGE;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<String> handle(MethodArgumentNotValidException e) {
        log.error("Invalid user input: {}", e.getMessage());
        return e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handle(HttpMessageNotReadableException e) {

        Throwable rootCause = Optional.ofNullable(e.getRootCause()).orElse(e);
        log.error("Failed to read request body: {}", rootCause.getMessage(), rootCause);
        return Optional.ofNullable(rootCause.getMessage())
                       .orElse("Incorrect JSON or invalid request body!");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handle(MethodArgumentTypeMismatchException e) {
        log.error("Incorrect argument type: {}", e.getMessage(), e);
        return BAD_REQUEST_MESSAGE;
    }

    @ExceptionHandler
    public String handleInvalidCurrencyResponseException(InvalidCurrencyResponseException ex) {
        List<String> errorMessages = ex.getErrorMessages();
        log.error("Invalid client currency response: {}", errorMessages);
        return BAD_REQUEST_MESSAGE;
    }
}

