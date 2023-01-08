package com.skiply.system.payment.domain.event;

import com.skiply.system.common.domain.event.DomainEvent;
import com.skiply.system.payment.domain.model.PaymentTransaction;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.OffsetDateTime;

@Getter
@SuperBuilder
@RequiredArgsConstructor
public abstract sealed class PaymentEvent implements DomainEvent<PaymentTransaction>
        permits PaymentFailedEvent, PaymentSucceedEvent {
    protected final PaymentTransaction paymentTransaction;
    protected final OffsetDateTime createdAt;
}
