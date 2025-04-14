package com.idftechnology.transactionlimitsservice.core.platform.validation.api;

import com.idftechnology.transactionlimitsservice.core.platform.validation.constraint.ExpenseCategoryValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = ExpenseCategoryValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@NotBlank(message = "Expense category type can't be blank")
public @interface ExpenseCategoryValid {

    String message() default "Invalid expense category";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
