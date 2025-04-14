package com.idftechnology.transactionlimitsservice.core.config;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.idftechnology.transactionlimitsservice.core.platform.util.jackson.CustomOffsetDateTimeDeserializer;
import com.idftechnology.transactionlimitsservice.core.platform.util.jackson.CustomOffsetDateTimeSerializer;
import com.idftechnology.transactionlimitsservice.core.platform.util.jackson.CustomZoneOffsetDeserializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Configuration
public class JacksonConfig {

    public static final DateTimeFormatter OFFSET_DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX");

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return builder -> {

            SimpleModule module = new SimpleModule();

            module.addSerializer(OffsetDateTime.class,
                    new CustomOffsetDateTimeSerializer(OFFSET_DATE_TIME_FORMATTER));
            module.addDeserializer(OffsetDateTime.class,
                    new CustomOffsetDateTimeDeserializer(OFFSET_DATE_TIME_FORMATTER));
            module.addDeserializer(ZoneOffset.class, new CustomZoneOffsetDeserializer());

            builder.modules(module);

            builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        };
    }
}
