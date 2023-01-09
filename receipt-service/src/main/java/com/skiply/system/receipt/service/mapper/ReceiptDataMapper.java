package com.skiply.system.receipt.service.mapper;

import com.skiply.system.common.domain.model.valueobject.Money;
import com.skiply.system.common.messaging.kafka.message.payment.PaymentSuccessMessage;
import com.skiply.system.receipt.api.dto.receipt.ReceiptResponse;
import com.skiply.system.receipt.domain.model.ReceiptStatus;
import com.skiply.system.receipt.infrastructure.persistence.entity.PurchaseItemEntity;
import com.skiply.system.receipt.infrastructure.persistence.entity.ReceiptEntity;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Component
public class ReceiptDataMapper {

    public ReceiptResponse entityToResponse(ReceiptEntity entity) {
        return ReceiptResponse.builder()
                .paidBy(entity.getPaidBy())
                .transactionDetail(prepareTransactionDetail(entity))
                .studentInfo(prepareStudentInfo(entity))
                .purchaseDetail(preparePurchaseDetail(entity.getPurchaseItems()))
                .receiptStatus(entity.getStatus())
                .build();
    }

    private ReceiptResponse.TransactionDetail prepareTransactionDetail(ReceiptEntity entity) {
        return ReceiptResponse.TransactionDetail.builder()
                .paymentReferenceNumber(entity.getPaymentReferenceNumber())
                .cardNumber(entity.getCardNumber())
                .cardType(entity.getCardType())
                .datetime(entity.getTransactionTs())
                .build();
    }

    private ReceiptResponse.StudentInfo prepareStudentInfo(ReceiptEntity entity) {
        if(entity.getStatus() == ReceiptStatus.PENDING) {
            return null;
        }

        return ReceiptResponse.StudentInfo.builder()
                .studentId(entity.getStudentId())
                .name(entity.getStudentName())
                .Grade(entity.getStudentGrade())
                .build();
    }

    private ReceiptResponse.PurchaseDetail preparePurchaseDetail(List<PurchaseItemEntity> purchaseItems) {
        var items = purchaseItems.stream()
                .map(itemEntity -> ReceiptResponse.PurchaseDetail.PurchaseItem.builder()
                        .price(new Money(itemEntity.getItemPrice()))
                        .type(itemEntity.getFeeType())
                        .quantity(itemEntity.getQuantity())
                        .name(itemEntity.getItemName())
                        .build())
                .toList();
        var totalPrice = items.stream()
                .map(ReceiptResponse.PurchaseDetail.PurchaseItem::price)
                .reduce(Money.ZERO, Money::add);

        return ReceiptResponse.PurchaseDetail.builder()
                .totalPrice(totalPrice)
                .purchaseItems(items)
                .build();
    }

    public ReceiptEntity messageToEntity(PaymentSuccessMessage message) {
        var receiptEntity = ReceiptEntity.builder()
                .id(UUID.randomUUID())
                .createdAt(OffsetDateTime.now())
                .transactionTs(message.transactionDetail().transactionDateTime())
                .status(ReceiptStatus.PENDING)
                .cardNumber(message.transactionDetail().cardNumber())
                .cardType(message.transactionDetail().cardType())
                .paidBy(message.paidBy())
                .purchaseItems(preparePurchaseItemEntities(message.purchaseItems()))
                .paymentReferenceNumber(message.transactionDetail().paymentReferenceNumber())
                .studentId(message.studentId())
                .build();

        receiptEntity.getPurchaseItems()
                .forEach(purchaseItemEntity -> purchaseItemEntity.setReceipt(receiptEntity));

        return receiptEntity;
    }

    private List<PurchaseItemEntity> preparePurchaseItemEntities(
            List<PaymentSuccessMessage.PurchaseItem> purchaseItems) {

        return purchaseItems.stream()
                .map(purchaseItem -> PurchaseItemEntity.builder()
                        .id(UUID.randomUUID())
                        .feeType(purchaseItem.feeType())
                        .itemName(purchaseItem.name())
                        .itemPrice(purchaseItem.price().getAmount())
                        .quantity(purchaseItem.quantity())
                        .build())
                .toList();
    }
}
