package com.idftechnology.transactionlimitsservice.core.service.impl;

import com.idftechnology.transactionlimitsservice.api.dto.LimitCreateDto;
import com.idftechnology.transactionlimitsservice.api.dto.LimitOutDto;
import com.idftechnology.transactionlimitsservice.core.platform.exception.LimitNotFoundException;
import com.idftechnology.transactionlimitsservice.core.platform.mapper.LimitMapper;
import com.idftechnology.transactionlimitsservice.core.repository.api.LimitRepository;
import com.idftechnology.transactionlimitsservice.core.repository.entity.Limit;
import com.idftechnology.transactionlimitsservice.core.service.api.LimitService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LimitServiceImpl implements LimitService {

    private final LimitRepository repository;
    private final LimitMapper mapper;

    @Transactional
    @Override
    public void initiate(Long accountId, Map<String, BigDecimal> limitsByDefault, @NotNull ZoneOffset zone) {
        List<Limit> limits = limitsByDefault.entrySet()
                                            .stream()
                                            .map(es -> LimitCreateDto.builder()
                                                                     .currency("USD")
                                                                     .expenseCategory(es.getKey())
                                                                     .sum(es.getValue())
                                                                     .build())
                                            .map(d -> mapper.toEntity(d, accountId, zone))
                                            .toList();

        repository.saveAllAndFlush(limits);
    }

    @Transactional
    @Override
    public LimitOutDto add(Long accountId, LimitCreateDto dto, @NotBlank ZoneOffset zoneOffset) {
        Optional<Limit> optLimit = repository.findLimitByCategory(accountId, dto.getExpenseCategory());

        Limit oldLimit = optLimit.orElseThrow(() -> new LimitNotFoundException(
                String.format("No found limit {%s} for account id {%d} ", dto.getExpenseCategory(), accountId))
        );
        oldLimit.setDateTo(OffsetDateTime.now(zoneOffset).minusSeconds(1));
        repository.save(oldLimit);

        Limit entity = mapper.toEntity(dto, accountId, zoneOffset);
        Limit limit = repository.saveAndFlush(entity);
        return mapper.toDto(limit);
    }

    @Transactional(readOnly = true)
    @Override
    public List<LimitOutDto> getAll(Long accountId, Pageable pageable) {
        return repository.getActiveLimits(accountId, pageable)
                         .stream()
                         .map(mapper::toDto)
                         .toList();
    }

    @Override
    public Limit getByAccountIdAndExpenseCategory(Long accountFrom, String expenseCategory) {
        return repository.findLimitByCategory(accountFrom, expenseCategory)
                         .orElseThrow(() -> new LimitNotFoundException(
                                 String.format("No found limit {%s} for account id {%d} ", expenseCategory,
                                               accountFrom))
                         );
    }
}
