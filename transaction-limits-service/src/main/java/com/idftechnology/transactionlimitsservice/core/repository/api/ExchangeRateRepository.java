package com.idftechnology.transactionlimitsservice.core.repository.api;

import com.idftechnology.transactionlimitsservice.core.repository.entity.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {
}
