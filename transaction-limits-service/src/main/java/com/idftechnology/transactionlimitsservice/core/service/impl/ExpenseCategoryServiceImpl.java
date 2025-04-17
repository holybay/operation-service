package com.idftechnology.transactionlimitsservice.core.service.impl;

import com.idftechnology.transactionlimitsservice.core.repository.api.ExpenseCategoryRepository;
import com.idftechnology.transactionlimitsservice.core.repository.entity.ExpenseCategory;
import com.idftechnology.transactionlimitsservice.core.service.api.ExpenseCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ExpenseCategoryServiceImpl implements ExpenseCategoryService {

    private final ExpenseCategoryRepository repository;

    @Transactional(readOnly = true)
    @Override
    public boolean contains(String name) {
        return repository.findByName(name).isPresent();
    }

    @Transactional(readOnly = true)
    @Override
    public ExpenseCategory getByName(String name) {
        return repository.findByName(name)
                         .orElseThrow(() -> new NoSuchElementException("No expense category with name: " + name));
    }
}
