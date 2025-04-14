package com.idftechnology.transactionlimitsservice.core.service.impl;

import com.idftechnology.transactionlimitsservice.core.repository.api.ExpenseCategoryRepository;
import com.idftechnology.transactionlimitsservice.core.service.api.ExpenseCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ExpenseCategoryServiceImpl implements ExpenseCategoryService {

    private final ExpenseCategoryRepository repository;

    @Transactional(readOnly = true)
    @Override
    public boolean contains(String name) {
        return repository.findByName(name).isPresent();
    }
}
