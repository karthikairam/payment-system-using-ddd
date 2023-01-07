package com.skiply.system.payment.domain.model;

import com.skiply.system.common.domain.model.BaseModel;
import com.skiply.system.common.domain.model.valueobject.Money;
import com.skiply.system.common.domain.model.valueobject.PaymentTransactionId;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@SuperBuilder
public class PurchaseItem extends BaseModel<UUID> {
    private PaymentTransactionId paymentTransactionId;
    private final String feeType;
    private final String name;
    private final Integer quantity;
    private final Money price;

    public void initializePurchaseItem(PaymentTransactionId id) {
        this.id = UUID.randomUUID();
        this.paymentTransactionId = id;
    }
}
