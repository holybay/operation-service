package com.idftechnology.transactionlimitsservice.core.platform.validation.constraint;

import com.idftechnology.transactionlimitsservice.core.platform.validation.api.ExpenseCategoryValid;
import com.idftechnology.transactionlimitsservice.core.service.api.ExpenseCategoryService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExpenseCategoryValidator implements ConstraintValidator<ExpenseCategoryValid, String> {

    private final ExpenseCategoryService service;

    @Override
    public boolean isValid(String name, ConstraintValidatorContext context) {
        return service.contains(name);
    }
}
