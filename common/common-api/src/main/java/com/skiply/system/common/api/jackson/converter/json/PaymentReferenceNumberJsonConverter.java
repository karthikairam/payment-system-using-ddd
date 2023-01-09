package com.skiply.system.common.api.jackson.converter.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.skiply.system.common.domain.model.valueobject.PaymentReferenceNumber;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Locale;

@Component
public class PaymentReferenceNumberJsonConverter implements ValueObjectJsonConverter<PaymentReferenceNumber> {

    @Override
    public JsonDeserializer<PaymentReferenceNumber> getJsonDeserializer() {
        return new JsonDeserializer<>() {
            @Override
            public PaymentReferenceNumber deserialize(
                    JsonParser jsonParser,
                    DeserializationContext deserializationContext) throws IOException {

                final var value = jsonParser.getValueAsString();
                if (value == null) {
                    return null;
                }

                return new PaymentReferenceNumber(value);
            }
        };
    }

    @Override
    public JsonSerializer<PaymentReferenceNumber> getJsonSerializer() {
        return new JsonSerializer<>() {
            @Override
            public void serialize(
                    PaymentReferenceNumber paymentReferenceNumber,
                    JsonGenerator jsonGenerator,
                    SerializerProvider serializerProvider) throws IOException {
                if (paymentReferenceNumber == null) {
                    jsonGenerator.writeNull();
                } else {
                    jsonGenerator.writeString(paymentReferenceNumber.value());
                }
            }
        };
    }

    @Override
    public Formatter<PaymentReferenceNumber> getTypedFieldFormatter() {
        return new Formatter<>() {
            @Override
            public PaymentReferenceNumber parse(String text, Locale locale) {
                return new PaymentReferenceNumber(text);
            }

            @Override
            public String print(PaymentReferenceNumber object, Locale locale) {
                return object.value();
            }
        };
    }

    @Override
    public Class<PaymentReferenceNumber> getType() {
        return PaymentReferenceNumber.class;
    }
}
