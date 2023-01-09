package com.skiply.system.common.api.jackson.converter.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.skiply.system.common.domain.model.valueobject.MobileNumber;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Locale;

@Component
public class MobileNumberJsonConverter implements ValueObjectJsonConverter<MobileNumber> {

    @Override
    public JsonDeserializer<MobileNumber> getJsonDeserializer() {
        return new JsonDeserializer<>() {
            @Override
            public MobileNumber deserialize(
                    JsonParser jsonParser,
                    DeserializationContext deserializationContext) throws IOException {

                final var value = jsonParser.getValueAsString();
                if (value == null) {
                    return null;
                }

                return new MobileNumber(value);
            }
        };
    }

    @Override
    public JsonSerializer<MobileNumber> getJsonSerializer() {
        return new JsonSerializer<>() {
            @Override
            public void serialize(
                    MobileNumber mobileNumber,
                    JsonGenerator jsonGenerator,
                    SerializerProvider serializerProvider) throws IOException {
                if (mobileNumber == null) {
                    jsonGenerator.writeNull();
                } else {
                    jsonGenerator.writeString(mobileNumber.value());
                }
            }
        };
    }

    @Override
    public Formatter<MobileNumber> getTypedFieldFormatter() {
        return new Formatter<>() {
            @Override
            public MobileNumber parse(String text, Locale locale) {
                return new MobileNumber(text.toLowerCase(locale));
            }

            @Override
            public String print(MobileNumber object, Locale locale) {
                return object.value();
            }
        };
    }

    @Override
    public Class<MobileNumber> getType() {
        return MobileNumber.class;
    }
}
