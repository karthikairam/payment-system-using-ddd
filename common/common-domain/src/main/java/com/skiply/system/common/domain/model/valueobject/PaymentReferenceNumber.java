package com.skiply.system.common.domain.model.valueobject;

import com.skiply.system.common.domain.exception.DomainValidationException;

public record PaymentReferenceNumber(String value) implements ValueObject {
    public PaymentReferenceNumber(String value) {
        this.value = validateValue(value);
    }

    private String validateValue(String value) {
        if(!value.matches("^\\d{15,30}$")) {
            throw new DomainValidationException("Invalid paymentReferenceNumber");
        }
        return value;
    }
}
