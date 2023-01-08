package com.skiply.system.payment.domain.event;

import com.skiply.system.payment.domain.model.PaymentTransaction;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.OffsetDateTime;

@SuperBuilder
public final class PaymentSucceedEvent extends PaymentEvent {
    public PaymentSucceedEvent(PaymentTransaction paymentTransaction, OffsetDateTime createdAt) {
        super(paymentTransaction, createdAt);
    }
}
