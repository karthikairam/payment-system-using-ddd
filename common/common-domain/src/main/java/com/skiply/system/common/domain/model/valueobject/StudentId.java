package com.skiply.system.common.domain.model.valueobject;

public record StudentId(String value) implements ValueObject {

    public StudentId(final String value) {
        this.value = validateValue(value);
    }

    private static String validateValue(final String value) {
        if (value == null
                || value.length() > 20
                || !value.matches("^[a-zA-Z0-9]*$")) {
            throw new IllegalArgumentException("Invalid StudentId");
        }
        return value;
    }
}
