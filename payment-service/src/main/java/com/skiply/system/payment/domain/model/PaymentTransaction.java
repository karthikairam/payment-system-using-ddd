package com.skiply.system.payment.domain.model;

import com.skiply.system.common.domain.model.AggregateRoot;
import com.skiply.system.common.domain.model.valueobject.*;
import com.skiply.system.payment.domain.exception.PaymentDomainException;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@SuperBuilder
public class PaymentTransaction extends AggregateRoot<PaymentTransactionId> {
    private OffsetDateTime transactionDateTime;
    private PaymentTransactionStatus paymentTransactionStatus;
    private StudentId studentId;
    private final String paidBy;
    private PaymentReferenceNumber referenceNumber;
    private final IdempotencyKey idempotencyKey;
    private final CardDetail cardDetail;
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
        Money orderItemsTotal = purchaseItems.stream()
                .map(purchaseItem -> purchaseItem.getPrice().multiply(purchaseItem.getQuantity()))
                .reduce(Money.ZERO, Money::add);
        if (!totalPrice.equals(orderItemsTotal)) {
            throw new PaymentDomainException("Total price: " + totalPrice.getAmount()
                    + " is not equal to Purchase items total: " + orderItemsTotal.getAmount() + ".");
        }
    }

    private void validateTotalPrice() {
        if (totalPrice == null || !totalPrice.isGreaterThanZero()) {
            throw new PaymentDomainException("Total price must be greater than zero.");
        }
    }

    private void validateInitialPayment() {
        if (paymentTransactionStatus != null || getId() != null) {
            throw new PaymentDomainException("Payment is not in valid state for initialization.");
        }
        validateCardDetail();
    }

    private void validateCardDetail() {
        if(this.cardDetail.cardExpiry() == null) {
            throw new PaymentDomainException("Card Expiry is mandatory for initiating payment.");
        }
        
        if(this.cardDetail.cardCvv() == null) {
            throw new PaymentDomainException("Card CVV is mandatory for initiating payment.");
        }
    }

    public void initializePayment() {
        this.id = new PaymentTransactionId(UUID.randomUUID());
        this.transactionDateTime = OffsetDateTime.now();
        this.paymentTransactionStatus = PaymentTransactionStatus.PENDING;
        initializePurchaseItems();
    }

    private void initializePurchaseItems() {
        purchaseItems.forEach(purchaseItem -> purchaseItem.initializePurchaseItem(this.id));
    }

    public void pay(PaymentReferenceNumber referenceNumber) {
        if(this.paymentTransactionStatus != PaymentTransactionStatus.PENDING) {
            throw new PaymentDomainException("PaymentTransaction is not in a valid status");
        }
        this.referenceNumber = referenceNumber;
        this.paymentTransactionStatus = PaymentTransactionStatus.COMPLETED;
    }

    public void fail(String reason) {
        if(this.paymentTransactionStatus != PaymentTransactionStatus.PENDING) {
            throw new PaymentDomainException("PaymentTransaction is not in a valid status");
        }
        this.paymentTransactionStatus = PaymentTransactionStatus.FAILED;
        updateFailureMessages(reason);
    }

    private void updateFailureMessages(String reason) {
        if (reason != null) {
            if (this.failureMessages == null) {
                this.failureMessages = new ArrayList<>();
            }
            this.failureMessages.add(reason);
        }
    }
}
