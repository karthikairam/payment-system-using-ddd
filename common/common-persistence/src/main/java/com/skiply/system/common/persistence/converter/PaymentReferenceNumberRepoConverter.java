package com.skiply.system.common.persistence.converter;

import com.skiply.system.common.domain.model.valueobject.PaymentReferenceNumber;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class PaymentReferenceNumberRepoConverter implements AttributeConverter<PaymentReferenceNumber, String> {
    @Override
    public String convertToDatabaseColumn(PaymentReferenceNumber attribute) {
        return attribute.value();
    }

    @Override
    public PaymentReferenceNumber convertToEntityAttribute(String dbData) {
        return new PaymentReferenceNumber(dbData);
    }
}