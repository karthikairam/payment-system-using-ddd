package com.skiply.system.payment.service.impl;

import com.skiply.system.common.domain.model.valueobject.PaymentReferenceNumber;
import com.skiply.system.common.domain.model.valueobject.PaymentTransactionStatus;
import com.skiply.system.payment.api.mapper.PaymentTransactionMapper;
import com.skiply.system.payment.api.payment.CollectPaymentCommand;
import com.skiply.system.payment.api.payment.CollectPaymentResponse;
import com.skiply.system.payment.domain.exception.PaymentDomainException;
import com.skiply.system.payment.domain.model.PaymentTransaction;
import com.skiply.system.payment.domain.service.PaymentDomainService;
import com.skiply.system.payment.infrastructure.apiclient.PaymentGatewayProvider;
import com.skiply.system.payment.infrastructure.apiclient.dto.PaymentGatewayResponse;
import com.skiply.system.payment.infrastructure.apiclient.dto.PaymentGatewayStatus;
import com.skiply.system.payment.infrastructure.apiclient.dto.mapper.PaymentGatewayMapper;
import com.skiply.system.payment.infrastructure.messaging.publisher.PaymentPublisher;
import com.skiply.system.payment.infrastructure.persistence.entity.IdempotencyKeyEntity;
import com.skiply.system.payment.infrastructure.persistence.entity.PaymentTransactionEntity;
import com.skiply.system.payment.infrastructure.persistence.mapper.PaymentTransactionEntityMapper;
import com.skiply.system.payment.infrastructure.persistence.repository.IdempotencyKeyRepository;
import com.skiply.system.payment.infrastructure.persistence.repository.PaymentTransactionRepository;
import com.skiply.system.payment.service.PaymentApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentApplicationServiceImpl implements PaymentApplicationService {

    private final PaymentGatewayProvider paymentGatewayProvider;
    private final PaymentDomainService paymentDomainService;
    private final PaymentTransactionRepository paymentTransactionRepository;
    private final IdempotencyKeyRepository idempotencyKeyRepository;
    private final PaymentTransactionMapper paymentTransactionMapper;
    private final PaymentTransactionEntityMapper paymentTransactionEntityMapper;
    private final PaymentGatewayMapper paymentGatewayMapper;
    private final PaymentPublisher paymentPublisher;

    @Override
    @Transactional
    public CollectPaymentResponse collectPayment(CollectPaymentCommand command) {

        // validate whether we already received this payment my checking the IdempotencyKey
        return idempotencyKeyRepository.findById(command.idempotencyKey().value())
                .map(IdempotencyKeyEntity::getPaymentTransaction)
                .map(this::entityToResponse)
                .orElseGet(
                    // If the idempotency key is not present then validate and process the payment
                    () -> {
                        final var domainModel = paymentTransactionMapper.commandToDomainModel(command);
                        paymentDomainService.validateAndInitializePayment(domainModel);

                        PaymentGatewayResponse gatewayResponse = paymentGatewayProvider
                                .makePayment(paymentGatewayMapper.domainModelToClientRequest(domainModel));

                        if(gatewayResponse.status().equals(PaymentGatewayStatus.SUCCESS)) {
                            var event = paymentDomainService.
                                    paymentSucceeded(domainModel, gatewayResponse.referenceNumber());
                            //save the Aggregates to DB
                            paymentTransactionRepository.save(
                                    paymentTransactionEntityMapper.domainModelToEntity(domainModel));
                            // publish the success event
                            // TODO If time permits use Outbox pattern to be reliable
                            paymentPublisher.publish(event);

                            return domainModelToResponse(domainModel);
                        } else {
                            log.error("Payment failed in payment gateway. Reason: {}", gatewayResponse.failureReason());
                            // For now, no need to use PaymentFailedEvent, since no interested consumer at the moment
                            paymentDomainService.paymentFailed(domainModel, gatewayResponse.failureReason());
                            throw new PaymentDomainException(gatewayResponse.failureReason());
                        }
                    }
                );
    }

    private CollectPaymentResponse domainModelToResponse(PaymentTransaction paymentTransaction) {
        return toResponse(paymentTransaction.getReferenceNumber(), paymentTransaction.getPaymentTransactionStatus());
    }

    private CollectPaymentResponse entityToResponse(PaymentTransactionEntity entity) {
        log.info("This payment was already processed and payment transaction id is {}", entity.getId());
        return toResponse(entity.getReferenceNumber(), entity.getStatus());
    }

    private CollectPaymentResponse toResponse(PaymentReferenceNumber referenceNumber, PaymentTransactionStatus status) {
        return CollectPaymentResponse.builder()
                .referenceNumber(referenceNumber)
                .status(status)
                .build();
    }
}
