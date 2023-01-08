package com.skiply.system.receipt.service;

import com.skiply.system.common.domain.model.valueobject.PaymentReferenceNumber;
import com.skiply.system.receipt.api.dto.receipt.ReceiptResponse;
import com.skiply.system.receipt.infrastructure.persistence.repository.ReceiptEntityRepository;
import com.skiply.system.receipt.service.mapper.ReceiptDataMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

public interface ReceiptApplicationService {
    ReceiptResponse retrieve(PaymentReferenceNumber referenceNumber);
}

@Slf4j
@Component
@RequiredArgsConstructor
class DefaultReceiptApplicationService implements ReceiptApplicationService {
    private final ReceiptEntityRepository receiptRepository;
    private final ReceiptDataMapper receiptDataMapper;

    @Override
    public ReceiptResponse retrieve(final PaymentReferenceNumber referenceNumber) {
        log.info("Request for receipt retrieval received for payment reference number {}", referenceNumber);
        return receiptDataMapper.entityToResponse(
                receiptRepository.findByReferenceNumber(referenceNumber)
        );
    }

}
