package com.skiply.system.payment.infrastructure.apiclient.dto;

import com.skiply.system.payment.domain.model.IdempotencyKey;
import lombok.Builder;

/*
 * Since it's just a mock behaviour, we will only use cardNumber, expiry, cvv.
 */
@Builder
public record PaymentGatewayRequest(String cardNumber,
                                    String cardType,
                                    String cardExpiry,
                                    String cvv,
                                    IdempotencyKey key) {
}
