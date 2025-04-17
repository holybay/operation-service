package com.idftechnology.transactionlimitsservice.core.platform.util.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class CustomTwelveDataLocalDateDeserializer extends StdDeserializer<LocalDate> {

    private final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public CustomTwelveDataLocalDateDeserializer() {
        super(LocalDateTime.class);
    }

    @Override
    public LocalDate deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {
        return LocalDate.parse(jsonParser.getText(), FORMATTER);
    }
}
