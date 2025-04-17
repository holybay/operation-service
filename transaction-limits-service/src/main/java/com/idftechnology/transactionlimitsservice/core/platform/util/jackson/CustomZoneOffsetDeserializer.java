package com.idftechnology.transactionlimitsservice.core.platform.util.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;


public class CustomZoneOffsetDeserializer extends StdDeserializer<ZoneOffset> {

    public CustomZoneOffsetDeserializer() {
        super(OffsetDateTime.class);
    }

    @Override
    public ZoneOffset deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {
        return ZoneOffset.of(jsonParser.getText());
    }
}
