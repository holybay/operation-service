package com.idftechnology.transactionlimitsservice.core.repository.api;

import com.idftechnology.transactionlimitsservice.core.repository.entity.ExpenseCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExpenseCategoryRepository extends JpaRepository<ExpenseCategory, Long> {

    Optional<ExpenseCategory> findByName(String name);
}
