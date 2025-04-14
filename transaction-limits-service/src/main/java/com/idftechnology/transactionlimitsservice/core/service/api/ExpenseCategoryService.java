package com.idftechnology.transactionlimitsservice.core.service.api;

import com.idftechnology.transactionlimitsservice.core.repository.entity.ExpenseCategory;

public interface ExpenseCategoryService {

    boolean contains(String name);

    ExpenseCategory getByName(String name);

}
