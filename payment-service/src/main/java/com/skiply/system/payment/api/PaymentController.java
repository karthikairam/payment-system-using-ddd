package com.skiply.system.payment.api;

import com.skiply.system.payment.api.payment.CollectPaymentCommand;
import com.skiply.system.payment.api.payment.CollectPaymentResponse;
import com.skiply.system.payment.service.PaymentApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/payment")
public class PaymentController {
    private final PaymentApplicationService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CollectPaymentResponse collectPayment(@Valid @RequestBody CollectPaymentCommand command) {
        return service.collectPayment(command);
    }
}
