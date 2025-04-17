package com.idftechnology.transactionlimitsservice.core.platform.util.mapper;

import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Component
public class LimitMapperHelper {

    @Named("newLimitStart")
    public OffsetDateTime setLimitDateFrom(ZoneOffset offset) {
        return OffsetDateTime.now(offset);
    }

}
