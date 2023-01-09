package com.skiply.system.payment.infrastructure.messaging.publisher.mapper;

import com.skiply.system.common.messaging.kafka.message.payment.PaymentSuccessMessage;
import com.skiply.system.payment.domain.event.PaymentSucceedEvent;
import com.skiply.system.payment.domain.model.PaymentTransaction;
import com.skiply.system.payment.domain.model.PurchaseItem;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PaymentMessageMapper {

    public PaymentSuccessMessage paymentSucceedEventToMessage(PaymentSucceedEvent event) {
        return PaymentSuccessMessage
                .builder()
                .paidBy(event.getPaymentTransaction().getPaidBy())
                .studentId(event.getPaymentTransaction().getStudentId())
                .transactionDetail(prepareTransactionDetail(event.getPaymentTransaction()))
                .purchaseItems(preparePurchaseItems(event.getPaymentTransaction().getPurchaseItems()))
                .build();
    }

    private List<PaymentSuccessMessage.PurchaseItem> preparePurchaseItems(List<PurchaseItem> purchaseItems) {
        return purchaseItems.stream()
                .map(item -> PaymentSuccessMessage.PurchaseItem
                        .builder()
                        .price(item.getPrice())
                        .name(item.getName())
                        .feeType(item.getFeeType())
                        .quantity(item.getQuantity())
                        .build()
                )
                .toList();
    }

    private PaymentSuccessMessage.TransactionDetail prepareTransactionDetail(PaymentTransaction paymentTransaction) {
        return PaymentSuccessMessage.TransactionDetail.builder()
                .cardNumber(paymentTransaction.getCardDetail().cardNumber()) // TODO mask the card number
                .cardType(paymentTransaction.getCardDetail().cardType())
                .paymentReferenceNumber(paymentTransaction.getPaymentReferenceNumber())
                .transactionDateTime(paymentTransaction.getTransactionDateTime())
                .build();
    }
}
