package com.skiply.system.common.persistence.converter;

import com.skiply.system.common.domain.model.valueobject.MobileNumber;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class MobileNumberRepoConverter implements AttributeConverter<MobileNumber, String> {
    @Override
    public String convertToDatabaseColumn(MobileNumber attribute) {
        return attribute.value();
    }

    @Override
    public MobileNumber convertToEntityAttribute(String dbData) {
        return new MobileNumber(dbData);
    }
}