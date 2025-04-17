package com.idftechnology.transactionlimitsservice.core.platform.util;

import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAdjusters;

@Component
public class DateTimeUtil {

    public OffsetDateTime convertToMonthStart(OffsetDateTime source) {
        return source.withOffsetSameInstant(ZoneOffset.UTC)
                     .with(TemporalAdjusters.firstDayOfMonth())
                     .with(LocalTime.MIN);
    }

    public OffsetDateTime convertToMonthEnd(OffsetDateTime source) {
        return source.withOffsetSameInstant(ZoneOffset.UTC)
                     .with(TemporalAdjusters.lastDayOfMonth())
                     .with(LocalTime.MAX);
    }
}
