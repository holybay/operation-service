package com.idftechnology.transactionlimitsservice.core.platform.mapper;

import com.idftechnology.transactionlimitsservice.api.dto.LimitCreateDto;
import com.idftechnology.transactionlimitsservice.api.dto.LimitOutDto;
import com.idftechnology.transactionlimitsservice.core.platform.util.mapper.BaseMapperHelper;
import com.idftechnology.transactionlimitsservice.core.platform.util.mapper.LimitMapperHelper;
import com.idftechnology.transactionlimitsservice.core.repository.entity.Limit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(config = BaseMapperConfig.class,
        uses = {BaseMapperHelper.class, LimitMapperHelper.class})
public interface LimitMapper {

    @Mapping(source = "expenseCategory.name", target = "expenseCategory")
    LimitOutDto toDto(Limit e);

    @Mapping(target = "accountFrom", source = "accountFrom")
    @Mapping(source = "d.zone", target = "dateFrom", qualifiedByName = "newLimitStart")
    @Mapping(target = "dateTo", ignore = true)
    @Mapping(source = "d.currency", target = "currency", qualifiedByName = "mapToCurrency")
    @Mapping(target = "expenseCategory", qualifiedByName = "mapCategoryByName")
    Limit toEntity(LimitCreateDto d, Long accountFrom);

}
