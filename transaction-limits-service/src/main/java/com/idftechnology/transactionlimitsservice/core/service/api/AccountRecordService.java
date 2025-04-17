package com.idftechnology.transactionlimitsservice.core.service.api;

public interface AccountRecordService {

    void activate(Long accountId);

    boolean isActive(Long accountId);
}
