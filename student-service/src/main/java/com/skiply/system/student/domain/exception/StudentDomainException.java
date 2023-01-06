package com.skiply.system.student.domain.exception;

import com.skiply.system.common.domain.exception.DomainException;

public class StudentDomainException extends DomainException {

    public StudentDomainException(String message) {
        super(message);
    }

    public StudentDomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
