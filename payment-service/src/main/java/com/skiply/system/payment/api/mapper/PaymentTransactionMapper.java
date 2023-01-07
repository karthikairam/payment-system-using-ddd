package com.skiply.system.payment.api.mapper;

import com.skiply.system.payment.api.payment.CollectPaymentCommand;
import com.skiply.system.payment.domain.model.PaymentTransaction;

public class PaymentTransactionMapper {

    public PaymentTransaction commandToDomainModel(CollectPaymentCommand command) {
        return PaymentTransaction.builder()
                .paidBy(command.paidBy())
                .cardCvv(command.)
                .build();
    }
}
