package com.idftechnology.transactionlimitsservice.core.repository.api;

import com.idftechnology.transactionlimitsservice.core.repository.entity.Limit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LimitRepository extends JpaRepository<Limit, Long> {

    @Query("""
            SELECT l
            FROM Limit l
            WHERE l.accountFrom = :account
            AND l.expenseCategory.name = :category
            AND l.dateTo IS NULL""")
    Optional<Limit> findLimitByCategory(@Param("account") Long accountFrom,
                                        @Param("category") String expenseCategory);

    @Query("SELECT l FROM Limit l WHERE l.accountFrom = :account AND l.dateTo IS NULL")
    Page<Limit> getActiveLimits(@Param("account") Long accountFrom, Pageable pageable);

}
