package com.skiply.system.receipt.api;

import com.skiply.system.common.domain.model.valueobject.PaymentReferenceNumber;
import com.skiply.system.receipt.api.dto.receipt.ReceiptResponse;
import com.skiply.system.receipt.service.ReceiptApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/v1/receipts")
@RequiredArgsConstructor
public class ReceiptController {

    private final ReceiptApplicationService service;

    @GetMapping
    public ResponseEntity<ReceiptResponse> retrieve(
            @RequestParam(value = "referenceNumber") PaymentReferenceNumber referenceNumber) {

        return ResponseEntity.ok(service.retrieve(referenceNumber));
    }
}
