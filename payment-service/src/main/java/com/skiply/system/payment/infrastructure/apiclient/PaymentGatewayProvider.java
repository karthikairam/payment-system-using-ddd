package com.skiply.system.payment.infrastructure.apiclient;

import com.skiply.system.common.domain.model.valueobject.PaymentReferenceNumber;
import com.skiply.system.payment.infrastructure.apiclient.dto.PaymentGatewayRequest;
import com.skiply.system.payment.infrastructure.apiclient.dto.PaymentGatewayResponse;
import com.skiply.system.payment.infrastructure.apiclient.dto.PaymentGatewayStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Random;
import java.util.Set;

public interface PaymentGatewayProvider {
    PaymentGatewayResponse makePayment(PaymentGatewayRequest request);
}

@RequiredArgsConstructor
@Component
class MockPaymentGatewayProvider implements PaymentGatewayProvider {

    private static final Set<String> supportedCardTypes = Set.of("MC", "VI");

    private final PaymentReferenceNumberGenerator paymentReferenceNumberGenerator;

    @Override
    public PaymentGatewayResponse makePayment(PaymentGatewayRequest request) {
        final var responseBuilder = PaymentGatewayResponse.builder();
        if(!supportedCardTypes.contains(request.cardType())) {
            responseBuilder.status(PaymentGatewayStatus.FAILED)
                    .failureReason("CardType " + request.cardType() + " is not supported");
        } else {
            responseBuilder.status(PaymentGatewayStatus.SUCCESS)
                    .paymentReferenceNumber(new PaymentReferenceNumber(paymentReferenceNumberGenerator.generateReceiptNumber()));
        }

        return responseBuilder.build();
    }
}

@Component
class PaymentReferenceNumberGenerator {
    private final long random = new Random().nextLong(100000, 1000000);

    public synchronized String generateReceiptNumber() {
        long timestamp = Instant.now().toEpochMilli();
        return String.format("%s%s", timestamp, random);
    }
}

