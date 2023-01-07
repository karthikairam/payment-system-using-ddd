package com.skiply.system.payment.service.impl;

import com.skiply.system.payment.api.payment.CollectPaymentCommand;
import com.skiply.system.payment.api.payment.CollectPaymentResponse;
import com.skiply.system.payment.infrastructure.apiclient.PaymentGatewayProvider;
import com.skiply.system.payment.infrastructure.persistence.repository.PaymentTransactionRepository;
import com.skiply.system.payment.service.PaymentApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentApplicationServiceImpl implements PaymentApplicationService {

    private final PaymentGatewayProvider paymentGatewayProvider;
    private final PaymentTransactionRepository transactionRepository;

    @Override
    public CollectPaymentResponse collectPayment(CollectPaymentCommand command) {

        //validateAndInitiatePayment

        //repository to save this

        return null;
    }
}
