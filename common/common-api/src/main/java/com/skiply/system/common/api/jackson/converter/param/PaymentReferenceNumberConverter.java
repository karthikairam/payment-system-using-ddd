package com.skiply.system.common.api.jackson.converter.param;

import com.skiply.system.common.domain.model.valueobject.PaymentReferenceNumber;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PaymentReferenceNumberConverter implements Converter<String, PaymentReferenceNumber> {

    @Override
    public PaymentReferenceNumber convert(String source) {
        return new PaymentReferenceNumber(source);
    }
}
