package com.skiply.system.common.domain.exception;

public class DomainEntityNotFoundException extends DomainException {
    public DomainEntityNotFoundException(String message) {
        super(message);
    }

    public DomainEntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
