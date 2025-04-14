package com.idftechnology.transactionlimitsservice.core.platform.validation.api;

import com.idftechnology.transactionlimitsservice.core.platform.validation.constraint.CurrencyValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = CurrencyValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@NotBlank(message = "Currency type can't be blank")
public @interface CurrencyValid {

    String message() default "Invalid currency code";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
