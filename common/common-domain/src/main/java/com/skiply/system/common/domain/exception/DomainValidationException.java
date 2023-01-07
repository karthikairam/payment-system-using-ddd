package com.skiply.system.common.domain.exception;

public class DomainValidationException extends DomainException {
    public DomainValidationException(String message) {
        super(message);
    }

    public DomainValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
