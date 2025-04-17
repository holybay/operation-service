package com.idftechnology.transactionlimitsservice.core.platform.util.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;


public class CustomOffsetDateTimeDeserializer extends StdDeserializer<OffsetDateTime> {

    private final DateTimeFormatter formatter;

    public CustomOffsetDateTimeDeserializer(DateTimeFormatter formatter) {
        super(OffsetDateTime.class);
        this.formatter = formatter;
    }

    @Override
    public OffsetDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {
        return OffsetDateTime.parse(jsonParser.getText(), formatter);
    }
}
