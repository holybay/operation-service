package com.idftechnology.transactionlimitsservice.core.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "exchange_rates")
public class ExchangeRate extends BaseEntity {

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "currency_from")
    private Currency from;

    @Column(name = "currency_to")
    private Currency to;

    @Column(name = "close_rate", precision = 18, scale = 6)
    private BigDecimal closeRate;

    @Column(name = "previous_close_rate", precision = 18, scale = 6)
    private BigDecimal previousCloseRate;
}
