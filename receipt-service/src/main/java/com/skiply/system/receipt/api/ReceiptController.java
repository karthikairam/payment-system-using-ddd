package com.skiply.system.receipt.api;

import com.skiply.system.common.domain.model.valueobject.PaymentReferenceNumber;
import com.skiply.system.receipt.api.dto.receipt.ReceiptResponse;
import com.skiply.system.receipt.domain.model.ReceiptStatus;
import com.skiply.system.receipt.domain.model.exception.ReceiptNotFoundDomainException;
import com.skiply.system.receipt.service.ReceiptApplicationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/v1/receipts")
@RequiredArgsConstructor
@Validated
@Tag(name = "Receipt Management APIs",
        description = "The API is used to retrieve receipts for a given payment reference number in the skiply system.")
public class ReceiptController {

    private final ReceiptApplicationService service;

    @GetMapping
    public ResponseEntity<ReceiptResponse> retrieve(
            @RequestParam(value = "paymentReferenceNumber")
            PaymentReferenceNumber paymentReferenceNumber
    ) {
        return service.retrieve(paymentReferenceNumber)
                .map(this::prepareRespectiveResponse)
                .orElseThrow(() -> new ReceiptNotFoundDomainException("Receipt not found for given reference number"));
    }

    private ResponseEntity<ReceiptResponse> prepareRespectiveResponse(ReceiptResponse receiptResponse) {
        if (receiptResponse.receiptStatus() == ReceiptStatus.COMPLETED) {
            return ResponseEntity.ok(receiptResponse);
        } else {
            /* When the status is PENDING the HTTP status code should be 202.
               Meaning it is accepted already and in progress as per REST principle */
            return ResponseEntity.accepted().body(receiptResponse);
        }
    }
}
