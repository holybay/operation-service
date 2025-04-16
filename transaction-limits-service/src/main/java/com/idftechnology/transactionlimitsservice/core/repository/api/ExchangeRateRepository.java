package com.idftechnology.transactionlimitsservice.core.repository.api;

import com.idftechnology.transactionlimitsservice.core.repository.entity.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {

    @Query("""
            SELECT er FROM ExchangeRate er
            "WHERE er.from.currencyCode IN :fromList
            "AND er.to.currencyCode IN :toList
            "AND er.date IN :dates""")
    List<ExchangeRate> findRatesByPairsAndDates(@Param("fromList") Iterable<String> fromList,
                                                @Param("toList") Iterable<String> toList,
                                                @Param("dates") Iterable<LocalDate> dates);
}
