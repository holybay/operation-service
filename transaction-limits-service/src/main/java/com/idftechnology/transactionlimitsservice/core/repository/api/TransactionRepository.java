package com.idftechnology.transactionlimitsservice.core.repository.api;

import com.idftechnology.transactionlimitsservice.core.repository.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query(value = """
            SELECT t
            FROM Transaction t
            WHERE t.accountFrom = :account
            AND t.expenseCategory.name = :category
            AND t.dateTime BETWEEN :start AND :end
            ORDER BY t.dateTime DESC
            """)
    List<Transaction> findAllByLimitBetween(@Param("account") Long accountId,
                                            @Param("category") String expenseCategory,
                                            @Param("start") OffsetDateTime start,
                                            @Param("end") OffsetDateTime end);

    @Query(value = """
            SELECT t
            FROM Transaction t
            WHERE t.accountFrom = :account
            AND t.expenseCategory.name = :category
            AND t.dateTime BETWEEN :start AND :end
            AND t.limitExceeded = TRUE
            ORDER BY t.dateTime DESC
            """)
    Page<Transaction> findAllByAccountFrom(@Param("account") Long accountFrom,
                                           @Param("category") String expenseCategory,
                                           @Param("start") OffsetDateTime start,
                                           @Param("end") OffsetDateTime end,
                                           Pageable pageable);

}
