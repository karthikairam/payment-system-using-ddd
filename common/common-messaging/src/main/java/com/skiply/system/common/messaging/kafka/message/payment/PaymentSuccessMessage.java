package com.skiply.system.common.messaging.kafka.message.payment;

import com.skiply.system.common.domain.model.valueobject.Money;
import com.skiply.system.common.domain.model.valueobject.PaymentReferenceNumber;
import com.skiply.system.common.domain.model.valueobject.StudentId;
import com.skiply.system.common.messaging.kafka.message.KafkaMessage;
import lombok.Builder;

import java.time.OffsetDateTime;
import java.util.List;

@Builder
public record PaymentSuccessMessage(
    String paidBy,
    StudentId studentId,
    TransactionDetail transactionDetail,
    List<PurchaseItem> purchaseItems
) implements KafkaMessage {

    @Builder
    public record TransactionDetail(
            OffsetDateTime transactionDateTime,
            PaymentReferenceNumber paymentReferenceNumber,
            String cardNumber,
            String cardType) { }

    @Builder
    public record PurchaseItem(
            String feeType,
            String name,
            Integer quantity,
            Money price
    ) { }
}
