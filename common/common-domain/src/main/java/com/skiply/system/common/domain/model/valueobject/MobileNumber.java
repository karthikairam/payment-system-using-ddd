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
        if (trimmedValue.length() > 20
                || !trimmedValue.matches("^(?:\\+|00)?(?:971)?[\\s.-]?(?:\\(?[0-9]{2}\\)?[\\s.-]?)?[0-9]{7}$")) {
            throw new IllegalArgumentException("Invalid MobileNumber");
        }
        return trimmedValue;
    }
}
