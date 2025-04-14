package com.idftechnology.transactionlimitsservice.core.service.impl;

import com.idftechnology.transactionlimitsservice.api.dto.LimitCreateDto;
import com.idftechnology.transactionlimitsservice.api.dto.LimitOutDto;
import com.idftechnology.transactionlimitsservice.core.platform.mapper.LimitMapper;
import com.idftechnology.transactionlimitsservice.core.repository.api.LimitRepository;
import com.idftechnology.transactionlimitsservice.core.repository.entity.Limit;
import com.idftechnology.transactionlimitsservice.core.service.api.LimitService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
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
                                                                     .zone(zone)
                                                                     .currency("USD")
                                                                     .expenseCategory(es.getKey())
                                                                     .sum(es.getValue())
                                                                     .build())
                                            .map(d -> mapper.toEntity(d, accountId))
                                            .toList();

        repository.saveAllAndFlush(limits);
    }

    @Transactional
    @Override
    public LimitOutDto add(Long accountId, LimitCreateDto dto) {
        Optional<Limit> optLimit = repository.findLimitByCategory(accountId, dto.getExpenseCategory());
        optLimit.ifPresent(l -> {
            l.setDateTo(OffsetDateTime.now(dto.getZone()).minusSeconds(1));
            repository.save(l);
        });
        Limit entity = mapper.toEntity(dto, accountId);
        Limit limit = repository.saveAndFlush(entity);
        return mapper.toDto(limit);
    }
}
