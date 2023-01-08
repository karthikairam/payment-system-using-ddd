package com.skiply.system.common.api.jackson.converter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.skiply.system.common.domain.model.valueobject.Money;
import com.skiply.system.common.domain.model.valueobject.Money;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Locale;

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
    public Formatter<Money> getTypedFieldFormatter() {
        return new Formatter<>() {
            @Override
            public Money parse(String text, Locale locale) {
                return new Money(new BigDecimal(text));
            }

            @Override
            public String print(Money object, Locale locale) {
                return object.value().toString();
            }
        };
    }

    @Override
    public Class<Money> getType() {
        return Money.class;
    }
}
