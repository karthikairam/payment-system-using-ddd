package com.skiply.system.payment.domain.model;

import com.skiply.system.common.domain.model.AggregateRoot;
import com.skiply.system.common.domain.model.valueobject.Money;
import com.skiply.system.common.domain.model.valueobject.PaymentReferenceNumber;
import com.skiply.system.common.domain.model.valueobject.PaymentTransactionId;
import com.skiply.system.common.domain.model.valueobject.PaymentTransactionStatus;
import com.skiply.system.payment.domain.exception.PaymentDomainException;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@SuperBuilder
public class PaymentTransaction extends AggregateRoot<PaymentTransactionId> {
    private OffsetDateTime transactionDateTime;
    private PaymentTransactionStatus paymentTransactionStatus;
    private final String paidBy;
    private PaymentReferenceNumber referenceNumber;
    private final String cardNumber;
    private final String cardType;
    private final String cardCvv;
    private final Money totalPrice;
    private final List<PurchaseItem> purchaseItems;
    List<String> failureMessages;

    /*
    * All the domain level logic within this Aggregate Root are listed below
    * */
    public void validatePayment() {
        validateInitialPayment();
        validateTotalPrice();
        validatePurchaseItemPrice();
    }

    private void validatePurchaseItemPrice() {
        Money orderItemsTotal = purchaseItems.stream().map(PurchaseItem::getPrice).reduce(Money.ZERO, Money::add);
        if (!totalPrice.equals(orderItemsTotal)) {
            throw new PaymentDomainException("Total price: " + totalPrice.getAmount()
                    + " is not equal to Purchase items total: " + orderItemsTotal.getAmount() + "!");
        }
    }

    private void validateTotalPrice() {
        if (totalPrice == null || !totalPrice.isGreaterThanZero()) {
            throw new PaymentDomainException("Total price must be greater than zero!");
        }
    }

    private void validateInitialPayment() {
        if (paymentTransactionStatus != null || getId() != null) {
            throw new PaymentDomainException("Payment is not in valid state for initialization!");
        }
    }

    public void initializePayment() {
        this.id = new PaymentTransactionId(UUID.randomUUID());
        this.paymentTransactionStatus = PaymentTransactionStatus.PENDING;
        initializePurchaseItems();
    }

    private void initializePurchaseItems() {
        purchaseItems.forEach(purchaseItem -> purchaseItem.initializePurchaseItem(this.id));
    }

    public void pay() {
        if(this.paymentTransactionStatus != PaymentTransactionStatus.PENDING) {
            throw new PaymentDomainException("PaymentTransaction is not in a valid status");
        }
        this.paymentTransactionStatus = PaymentTransactionStatus.COMPLETED;
    }

    public void markAsFailed(List<String> failureMessages) {
        if(this.paymentTransactionStatus != PaymentTransactionStatus.PENDING) {
            throw new PaymentDomainException("PaymentTransaction is not in a valid status");
        }
        this.paymentTransactionStatus = PaymentTransactionStatus.FAILED;
        updateFailureMessages(failureMessages);
    }

    private void updateFailureMessages(List<String> failureMessages) {
        if (this.failureMessages != null && failureMessages != null) {
            this.failureMessages.addAll(failureMessages.stream().filter(message -> !message.isEmpty()).toList());
        }
        if (this.failureMessages == null) {
            this.failureMessages = failureMessages;
        }
    }
}
