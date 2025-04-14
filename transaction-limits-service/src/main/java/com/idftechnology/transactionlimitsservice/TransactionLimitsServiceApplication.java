package com.idftechnology.transactionlimitsservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan("com.idftechnology.transactionlimitsservice.core.config.property")
public class TransactionLimitsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TransactionLimitsServiceApplication.class, args);
    }

}
