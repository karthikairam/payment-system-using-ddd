package com.skiply.system.payment.service.impl;

import com.skiply.system.common.domain.exception.DomainValidationException;
import com.skiply.system.common.domain.model.valueobject.PaymentReferenceNumber;
import com.skiply.system.payment.api.mapper.PaymentTransactionMapper;
import com.skiply.system.payment.api.payment.CollectPaymentCommand;
import com.skiply.system.payment.api.payment.CollectPaymentResponse;
import com.skiply.system.payment.infrastructure.apiclient.PaymentGatewayProvider;
import com.skiply.system.payment.infrastructure.persistence.entity.IdempotencyKeyEntity;
import com.skiply.system.payment.infrastructure.persistence.repository.IdempotencyKeyRepository;
import com.skiply.system.payment.infrastructure.persistence.repository.PaymentTransactionRepository;
import com.skiply.system.payment.service.PaymentApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class PaymentApplicationServiceImpl implements PaymentApplicationService {

    private final PaymentGatewayProvider paymentGatewayProvider;
    private final PaymentTransactionRepository paymentTransactionRepository;
    private final IdempotencyKeyRepository idempotencyKeyRepository;
    private final PaymentTransactionMapper paymentTransactionMapper;

    @Override
    @Transactional
    public CollectPaymentResponse collectPayment(CollectPaymentCommand command) {

        //validate whether we already received this payment my checking the IdempotencyKey

        return idempotencyKeyRepository.findById(command.idempotencyKey().value())
                .map(this::prepareCollectPaymentResponse)
                .orElseGet(
                    // If the idempotency key is not present then validate and process the payment
                    () -> {

                        return null;
                    }
                );
    }

    private CollectPaymentResponse prepareCollectPaymentResponse(IdempotencyKeyEntity idempotencyKeyEntity) {
        return CollectPaymentResponse.builder()
                .referenceNumber(new PaymentReferenceNumber(idempotencyKeyEntity.getPaymentTransaction().getReferenceNumber()))
                .status(idempotencyKeyEntity.getPaymentTransaction().getStatus())
                .build();
    }
}
