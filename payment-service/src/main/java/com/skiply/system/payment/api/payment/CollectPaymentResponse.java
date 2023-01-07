package com.skiply.system.payment.api.payment;

import com.skiply.system.common.domain.model.valueobject.PaymentReferenceNumber;
import com.skiply.system.common.domain.model.valueobject.PaymentTransactionStatus;
import lombok.Builder;

@Builder
public record CollectPaymentResponse(PaymentReferenceNumber referenceNumber, PaymentTransactionStatus status) {
}
