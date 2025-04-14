package com.idftechnology.transactionlimitsservice.core.repository.api;

import com.idftechnology.transactionlimitsservice.core.repository.entity.AccountRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRecordRepository extends JpaRepository<AccountRecord, Long> {
}
