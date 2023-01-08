package com.skiply.system.receipt.domain.model.exception;

import com.skiply.system.common.domain.exception.DomainEntityNotFoundException;

public class ReceiptNotFoundDomainException extends DomainEntityNotFoundException {
    public ReceiptNotFoundDomainException(String message) {
        super(message);
    }
}
