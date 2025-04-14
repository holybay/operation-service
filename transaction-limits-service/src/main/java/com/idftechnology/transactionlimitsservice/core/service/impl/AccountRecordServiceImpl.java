package com.idftechnology.transactionlimitsservice.core.service.impl;

import com.idftechnology.transactionlimitsservice.core.repository.api.AccountRecordRepository;
import com.idftechnology.transactionlimitsservice.core.repository.entity.AccountRecord;
import com.idftechnology.transactionlimitsservice.core.service.api.AccountRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountRecordServiceImpl implements AccountRecordService {

    private final AccountRecordRepository repository;

    @Transactional
    @Override
    public void activate(Long accountId) {
        repository.saveAndFlush(AccountRecord.builder()
                                             .accountId(accountId)
                                             .isActivated(true)
                                             .build());
    }

    @Transactional(readOnly = true)
    @Override
    public boolean isActive(Long accountId) {
        return repository.existsById(accountId);
    }
}
