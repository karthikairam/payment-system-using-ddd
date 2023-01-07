package com.skiply.system.common.domain.model.valueobject;

public record MobileNumber(String value) implements ValueObject {

    public MobileNumber(final String value) {
        this.value = validateValue(value);
    }

    private static String validateValue(final String value) {
        if (value == null) {
            return null;
        }
        String trimmedValue = value.trim();
        if (!trimmedValue.matches("^(?:00971|\\+971|0)?\\d{9}$")) {
            throw new IllegalArgumentException("Invalid MobileNumber");
        }
        return trimmedValue;
    }
}
