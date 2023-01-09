package com.skiply.system.payment.infrastructure.persistence.mapper;

import com.skiply.system.payment.domain.model.IdempotencyKey;
import com.skiply.system.payment.domain.model.PaymentTransaction;
import com.skiply.system.payment.domain.model.PurchaseItem;
import com.skiply.system.payment.infrastructure.persistence.entity.IdempotencyKeyEntity;
import com.skiply.system.payment.infrastructure.persistence.entity.PaymentTransactionEntity;
import com.skiply.system.payment.infrastructure.persistence.entity.PurchaseItemEntity;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Component
public class PaymentTransactionEntityMapper {
    public PaymentTransactionEntity domainModelToEntity(PaymentTransaction domainModel) {
        var paymentEntity = PaymentTransactionEntity.builder()
                .id(UUID.randomUUID())
                .paymentReferenceNumber(domainModel.getPaymentReferenceNumber())
                .cardNumber(maskCardNumber(domainModel.getCardDetail().cardNumber()))
                .cardType(domainModel.getCardDetail().cardType())
                .paidBy(domainModel.getPaidBy())
                .idempotencyKey(toIdempotencyKeyEntity(domainModel.getIdempotencyKey()))
                .status(domainModel.getPaymentTransactionStatus())
                .studentId(domainModel.getStudentId())
                .totalFeePaid(domainModel.getTotalPrice().getAmount())
                .createdAt(OffsetDateTime.now())
                .purchaseItems(preparePurchaseItemEntities(domainModel.getPurchaseItems()))
                .createdAt(OffsetDateTime.now())
                .build();

        paymentEntity.getIdempotencyKey().setPaymentTransaction(paymentEntity);
        paymentEntity.getPurchaseItems().forEach(purchaseItemEntity -> purchaseItemEntity.setPaymentTransaction(paymentEntity));

        return paymentEntity;
    }

    private List<PurchaseItemEntity> preparePurchaseItemEntities(List<PurchaseItem> purchaseItems) {
        return purchaseItems.stream()
                .map(purchaseItem -> PurchaseItemEntity.builder()
                        .id(UUID.randomUUID())
                        .feeType(purchaseItem.getFeeType())
                        .quantity(purchaseItem.getQuantity())
                        .itemName(purchaseItem.getName())
                        .itemPrice(purchaseItem.getPrice().getAmount())
                        .createdAt(OffsetDateTime.now())
                        .build())
                .toList();
    }

    private IdempotencyKeyEntity toIdempotencyKeyEntity(IdempotencyKey idempotencyKey) {
        return IdempotencyKeyEntity.builder()
                .key(idempotencyKey.value())
                .build();
    }

    private String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 4) {
            return "";
        }

        int maskLength = cardNumber.length() - 4;
        return "*".repeat(maskLength) +
                cardNumber.substring(maskLength);
    }
}
