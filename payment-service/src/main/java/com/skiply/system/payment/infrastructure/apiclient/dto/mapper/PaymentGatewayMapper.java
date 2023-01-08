package com.skiply.system.payment.infrastructure.apiclient.dto.mapper;

import com.skiply.system.payment.domain.model.PaymentTransaction;
import com.skiply.system.payment.infrastructure.apiclient.dto.PaymentGatewayRequest;
import org.springframework.stereotype.Component;

@Component
public class PaymentGatewayMapper {
    public PaymentGatewayRequest domainModelToClientRequest(PaymentTransaction domainModel) {
        return PaymentGatewayRequest.builder()
                .key(domainModel.getIdempotencyKey())
                .cvv(domainModel.getCardDetail().cardCvv())
                .cardExpiry(domainModel.getCardDetail().cardExpiry())
                .cardNumber(domainModel.getCardDetail().cardNumber())
                .cardType(domainModel.getCardDetail().cardType())
                .build();
    }
}
