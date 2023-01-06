package com.skiply.system.common.persistence.converter;

import com.skiply.system.common.domain.model.valueobject.StudentId;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class StudentIdRepoConverter implements AttributeConverter<StudentId, String> {
    @Override
    public String convertToDatabaseColumn(StudentId attribute) {
        return attribute.value();
    }

    @Override
    public StudentId convertToEntityAttribute(String dbData) {
        return new StudentId(dbData);
    }
}