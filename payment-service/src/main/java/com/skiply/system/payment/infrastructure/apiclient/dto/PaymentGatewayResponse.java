package com.skiply.system.payment.infrastructure.apiclient.dto;

import com.skiply.system.common.domain.model.valueobject.PaymentReferenceNumber;
import lombok.Builder;

@Builder
public record PaymentGatewayResponse(PaymentGatewayStatus status, String failureReason,
                                     PaymentReferenceNumber paymentReferenceNumber) {
}
