package com.skiply.system.common.api.jackson.converter.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.skiply.system.common.domain.model.valueobject.Money;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.DecimalFormat;

@Component
public class MoneyJsonConverter implements ValueObjectJsonConverter<Money> {

    @Override
    public JsonDeserializer<Money> getJsonDeserializer() {
        return new JsonDeserializer<>() {
            @Override
            public Money deserialize(
                    JsonParser jsonParser,
                    DeserializationContext deserializationContext) throws IOException {

                final var value = jsonParser.getDecimalValue();
                if (value == null) {
                    return null;
                }

                return new Money(value);
            }
        };
    }

    @Override
    public JsonSerializer<Money> getJsonSerializer() {
        return new JsonSerializer<>() {
            @Override
            public void serialize(
                    Money money,
                    JsonGenerator jsonGenerator,
                    SerializerProvider serializerProvider) throws IOException {
                if (money == null) {
                    jsonGenerator.writeNull();
                } else {
                    jsonGenerator.writeNumber(money.value());
                }
            }
        };
    }

    @Override
    public Class<Money> getType() {
        return Money.class;
    }
}
