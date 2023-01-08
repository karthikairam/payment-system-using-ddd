package com.skiply.system.payment.domain.model;

import com.skiply.system.common.domain.model.valueobject.ValueObject;
import lombok.Builder;

import java.util.Objects;

@Builder
public record CardDetail(String cardNumber,
        String cardType,
        String cardExpiry,
        String cardCvv) implements ValueObject {

    public CardDetail(String cardNumber,
                      String cardType,
                      String cardExpiry,
                      String cardCvv) {

        this.cardNumber = Objects.requireNonNull(cardNumber);
        this.cardType = Objects.requireNonNull(cardType);
        this.cardExpiry = cardExpiry;
        this.cardCvv = cardCvv;
    }
}