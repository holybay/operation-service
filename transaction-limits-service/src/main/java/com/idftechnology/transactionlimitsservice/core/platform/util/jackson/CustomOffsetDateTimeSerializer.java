package com.idftechnology.transactionlimitsservice.core.platform.util.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;


public class CustomOffsetDateTimeSerializer extends StdSerializer<OffsetDateTime> {

    private final DateTimeFormatter formatter;

    public CustomOffsetDateTimeSerializer(DateTimeFormatter formatter) {
        super(OffsetDateTime.class);
        this.formatter = formatter;
    }

    @Override
    public void serialize(OffsetDateTime date, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (date != null) {
            gen.writeString(date.format(formatter));
        }
    }
}
