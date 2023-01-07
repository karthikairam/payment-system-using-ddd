package com.skiply.system.payment.api.mapper;

import com.skiply.system.common.domain.model.valueobject.Money;
import com.skiply.system.payment.api.payment.CollectPaymentCommand;
import com.skiply.system.payment.domain.model.PaymentTransaction;
import com.skiply.system.payment.domain.model.PurchaseItem;

import java.util.List;

public class PaymentTransactionMapper {

    public PaymentTransaction commandToDomainModel(CollectPaymentCommand command) {
        return PaymentTransaction.builder()
                .paidBy(command.paidBy())
                .cardCvv(command.cardCvv())
                .cardType(command.cardType())
                .cardNumber(command.cardNumber())
                .totalPrice(new Money(command.totalPrice()))
                .purchaseItems(preparePurchaseItem(command.purchaseItems()))
                .build();
    }

    private List<PurchaseItem> preparePurchaseItem(List<CollectPaymentCommand.PurchaseItem> purchaseItems) {
        return purchaseItems.stream()
                .map(this::commandToPurchaseItem).toList();
    }

    private PurchaseItem commandToPurchaseItem(CollectPaymentCommand.PurchaseItem purchaseItemCommand) {
        return PurchaseItem.builder()
                .price(new Money(purchaseItemCommand.price()))
                .name(purchaseItemCommand.name())
                .quantity(purchaseItemCommand.quantity())
                .feeType(purchaseItemCommand.feeType())
                .build();
    }
}
