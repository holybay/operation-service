package com.idftechnology.transactionlimitsservice.core.platform.mapper;

import com.idftechnology.transactionlimitsservice.api.dto.TransactionCreateDto;
import com.idftechnology.transactionlimitsservice.api.dto.TransactionOutDto;
import com.idftechnology.transactionlimitsservice.core.platform.util.mapper.BaseMapperHelper;
import com.idftechnology.transactionlimitsservice.core.repository.entity.Limit;
import com.idftechnology.transactionlimitsservice.core.repository.entity.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = BaseMapperConfig.class,
        uses = {BaseMapperHelper.class})
public interface TransactionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(source = "d.currency", target = "currency", qualifiedByName = "mapToCurrency")
    @Mapping(target = "expenseCategory", qualifiedByName = "mapCategoryByName")
    @Mapping(target = "zone", qualifiedByName = "getZoneOffset", source = "d.dateTime")
    @Mapping(source = "limitExceeded", target = "limitExceeded")
    Transaction toEntity(TransactionCreateDto d, boolean limitExceeded);

    @Mapping(source = "e.accountFrom", target = "accountFrom")
    @Mapping(source = "e.accountTo", target = "accountTo")
    @Mapping(source = "e.sum", target = "sum")
    @Mapping(source = "e.currency", target = "currency")
    @Mapping(source = "e.expenseCategory.name", target = "expenseCategory")
    @Mapping(source = "l.sum", target = "limitSum")
    @Mapping(source = "l.currency", target = "limitCurrency")
    @Mapping(source = "l.dateFrom", target = "limitDateFrom")
    TransactionOutDto toDto(Transaction e, Limit l);
}
