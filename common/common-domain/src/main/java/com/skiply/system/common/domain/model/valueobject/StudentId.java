package com.skiply.system.common.domain.model.valueobject;

import com.skiply.system.common.domain.exception.DomainValidationException;

public record StudentId(String value) implements ValueObject {

    public StudentId(final String value) {
        this.value = validateValue(value);
    }

    private String validateValue(final String value) {
        if (value == null
                || value.length() > 20
                || !value.matches("^[a-zA-Z0-9]+$")) {
            throw new DomainValidationException("Invalid StudentId");
        }
        return value;
    }
}
