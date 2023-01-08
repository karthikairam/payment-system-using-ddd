package com.skiply.system.common.api.jackson.converter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.skiply.system.common.domain.model.valueobject.ReceiptId;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Locale;
import java.util.UUID;

@Component
public class ReceiptIdJsonConverter implements ValueObjectJsonConverter<ReceiptId> {

    @Override
    public JsonDeserializer<ReceiptId> getJsonDeserializer() {
        return new JsonDeserializer<>() {
            @Override
            public ReceiptId deserialize(
                    JsonParser jsonParser,
                    DeserializationContext deserializationContext) throws IOException {

                final var value = jsonParser.getValueAsString();
                if (value == null) {
                    return null;
                }

                return new ReceiptId(UUID.fromString(value));
            }
        };
    }

    @Override
    public JsonSerializer<ReceiptId> getJsonSerializer() {
        return new JsonSerializer<>() {
            @Override
            public void serialize(
                    ReceiptId receiptId,
                    JsonGenerator jsonGenerator,
                    SerializerProvider serializerProvider) throws IOException {
                if (receiptId == null) {
                    jsonGenerator.writeNull();
                } else {
                    jsonGenerator.writeString(receiptId.value().toString());
                }
            }
        };
    }

    @Override
    public Formatter<ReceiptId> getTypedFieldFormatter() {
        return new Formatter<>() {
            @Override
            public ReceiptId parse(String text, Locale locale) {
                return new ReceiptId(UUID.fromString(text));
            }

            @Override
            public String print(ReceiptId object, Locale locale) {
                return object.value().toString();
            }
        };
    }

    @Override
    public Class<ReceiptId> getType() {
        return ReceiptId.class;
    }
}
