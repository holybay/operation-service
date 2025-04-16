package com.idftechnology.transactionlimitsservice.core.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Currency;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "transactions")
public class Transaction extends BaseEntity {

    @Column(name = "account_from")
    private Long accountFrom;

    @Column(name = "account_to")
    private Long accountTo;

    @Column(name = "date_time")
    private OffsetDateTime dateTime;

    @Column(name = "date_time_zone")
    private String zone;

    @Column(name = "sum", precision = 18, scale = 2)
    private BigDecimal sum;

    @Column(name = "currency")
    private Currency currency;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private ExpenseCategory expenseCategory;

    @Column(name = "limit_exceeded")
    private boolean limitExceeded;

}
