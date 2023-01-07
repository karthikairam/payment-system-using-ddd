package com.skiply.system.payment.service;

import com.skiply.system.payment.api.payment.CollectPaymentCommand;
import com.skiply.system.payment.api.payment.CollectPaymentResponse;

public interface PaymentApplicationService {
    CollectPaymentResponse collectPayment(final CollectPaymentCommand command);
}
