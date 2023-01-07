package com.skiply.system.common.domain.model.valueobject;

import com.skiply.system.common.domain.exception.DomainValidationException;

public record MobileNumber(String value) implements ValueObject {

    public MobileNumber(final String value) {
        this.value = validateValue(value);
    }

    private String validateValue(final String value) {
        if (value == null) {
            return null;
        }
        String trimmedValue = value.trim();
        if (!trimmedValue.matches("^(?:00971|\\+971|0)?\\d{9}$")) {
            throw new DomainValidationException("Invalid MobileNumber");
        }
        return trimmedValue;
    }
}
