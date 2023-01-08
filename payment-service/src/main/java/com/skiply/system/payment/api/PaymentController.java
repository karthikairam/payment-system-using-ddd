package com.skiply.system.payment.api;

import com.skiply.system.payment.api.payment.CollectPaymentCommand;
import com.skiply.system.payment.api.payment.CollectPaymentResponse;
import com.skiply.system.payment.service.PaymentApplicationService;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/v1/payments")
@Tag(name = "Fee Payment APIs",
        description = "The API is used to pay the school fees for the students in the skiply system.")
public class PaymentController {
    private final PaymentApplicationService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CollectPaymentResponse collectPayment(@Valid @RequestBody CollectPaymentCommand command) {
        return service.collectPayment(command);
    }
}
