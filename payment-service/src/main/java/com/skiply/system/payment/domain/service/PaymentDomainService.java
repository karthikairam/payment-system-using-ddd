package com.skiply.system.payment.domain.service;

import com.skiply.system.common.domain.model.valueobject.PaymentReferenceNumber;
import com.skiply.system.payment.domain.event.PaymentFailedEvent;
import com.skiply.system.payment.domain.event.PaymentSucceedEvent;
import com.skiply.system.payment.domain.model.PaymentTransaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

public interface PaymentDomainService {
    void validateAndInitializePayment(PaymentTransaction paymentTransaction);

    PaymentSucceedEvent paymentSucceeded(PaymentTransaction paymentTransaction, PaymentReferenceNumber paymentReferenceNumber);

    PaymentFailedEvent paymentFailed(PaymentTransaction paymentTransaction, String reason);
}

/***
 * Domain logic orchestration happens here and if applicable it will respond with domain events.
 */
@Slf4j
@Component
class PaymentDomainServiceImpl implements PaymentDomainService {

    @Override
    public void validateAndInitializePayment(final PaymentTransaction paymentTransaction) {
        paymentTransaction.validatePayment();
        paymentTransaction.initializePayment();
        log.info("PaymentTransaction with id: {} is initiated.", paymentTransaction.getId().value());
    }

    @Override
    public PaymentSucceedEvent paymentSucceeded(final PaymentTransaction paymentTransaction,
                                                final PaymentReferenceNumber paymentReferenceNumber) {
        log.info("PaymentTransaction with id: {} is successful.", paymentTransaction.getId().value());
        paymentTransaction.pay(paymentReferenceNumber);
        return PaymentSucceedEvent.builder()
                .paymentTransaction(paymentTransaction)
                .createdAt(OffsetDateTime.now())
                .build();
    }

    @Override
    public PaymentFailedEvent paymentFailed(final PaymentTransaction paymentTransaction, String reason) {
        log.info("PaymentTransaction with id: {} is failure.", paymentTransaction.getId().value());
        paymentTransaction.fail(reason);
        return PaymentFailedEvent.builder()
                .paymentTransaction(paymentTransaction)
                .createdAt(OffsetDateTime.now())
                .build();
    }
}
