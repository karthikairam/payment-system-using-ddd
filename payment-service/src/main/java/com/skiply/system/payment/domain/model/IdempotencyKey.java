package com.skiply.system.payment.domain.model;

import com.skiply.system.common.domain.exception.DomainValidationException;
import com.skiply.system.common.domain.model.valueobject.ValueObject;

import java.util.Objects;

public record IdempotencyKey(String value) implements ValueObject {
    public IdempotencyKey(String value) {
        this.value = validate(value);
    }

    private String validate(String value) {
        if(Objects.isNull(value) || value.isBlank() || value.length() < 10) {
            throw new DomainValidationException("IdempotencyKey should be non blank & at least 10 chars");
        }
        return value;
    }
}
