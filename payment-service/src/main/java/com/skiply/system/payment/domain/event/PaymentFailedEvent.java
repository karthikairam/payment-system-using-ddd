package com.skiply.system.payment.domain.event;

import com.skiply.system.payment.domain.model.PaymentTransaction;
import lombok.experimental.SuperBuilder;

import java.time.OffsetDateTime;

@SuperBuilder
public final class PaymentFailedEvent extends PaymentEvent {
    public PaymentFailedEvent(PaymentTransaction paymentTransaction, OffsetDateTime createdAt) {
        super(paymentTransaction, createdAt);
    }
}
