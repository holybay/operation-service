package com.idftechnology.transactionlimitsservice.core.service.feign.client;

import com.idftechnology.transactionlimitsservice.core.service.dto.CurrencyResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "twelveDataClient", url = "${clients.currency.twelve-data.url}")
public interface TwelveDataClient {

    @GetMapping("/time_series")
    CurrencyResponseDto getRate(@RequestParam("symbol") String currencyPair,
                                @RequestParam("interval") String interval,
                                @RequestParam("outputsize") int outputSize,
                                @RequestParam("previous_close") boolean previousClose,
                                @RequestParam("apikey") String apiKey);

}
