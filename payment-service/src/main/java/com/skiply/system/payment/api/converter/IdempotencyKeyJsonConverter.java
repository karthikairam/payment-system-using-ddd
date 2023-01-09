package com.skiply.system.payment.api.converter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.skiply.system.common.api.jackson.converter.json.ValueObjectJsonConverter;
import com.skiply.system.payment.domain.model.IdempotencyKey;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Locale;

@Component
public class IdempotencyKeyJsonConverter implements ValueObjectJsonConverter<IdempotencyKey> {

    @Override
    public JsonDeserializer<IdempotencyKey> getJsonDeserializer() {
        return new JsonDeserializer<>() {
            @Override
            public IdempotencyKey deserialize(
                    JsonParser jsonParser,
                    DeserializationContext deserializationContext) throws IOException {

                final var value = jsonParser.getValueAsString();
                if (value == null) {
                    return null;
                }

                return new IdempotencyKey(value);
            }
        };
    }

    @Override
    public JsonSerializer<IdempotencyKey> getJsonSerializer() {
        return new JsonSerializer<>() {
            @Override
            public void serialize(
                    IdempotencyKey idempotencyKey,
                    JsonGenerator jsonGenerator,
                    SerializerProvider serializerProvider) throws IOException {
                if (idempotencyKey == null) {
                    jsonGenerator.writeNull();
                } else {
                    jsonGenerator.writeString(idempotencyKey.value());
                }
            }
        };
    }

    @Override
    public Formatter<IdempotencyKey> getTypedFieldFormatter() {
        return new Formatter<>() {
            @Override
            public IdempotencyKey parse(String text, Locale locale) {
                return new IdempotencyKey(text.toLowerCase(locale));
            }

            @Override
            public String print(IdempotencyKey object, Locale locale) {
                return object.value();
            }
        };
    }

    @Override
    public Class<IdempotencyKey> getType() {
        return IdempotencyKey.class;
    }
}
