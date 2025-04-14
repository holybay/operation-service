package com.idftechnology.transactionlimitsservice.core.platform.util.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.Currency;

public class CustomCurrencySerializer extends JsonSerializer<Currency> {

    @Override
    public void serialize(Currency value, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
        gen.writeString(value.getCurrencyCode());
    }
}
