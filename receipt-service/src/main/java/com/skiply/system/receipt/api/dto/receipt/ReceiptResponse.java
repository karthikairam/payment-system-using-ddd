package com.skiply.system.receipt.api.dto.receipt;

import com.skiply.system.common.domain.model.valueobject.Money;
import com.skiply.system.common.domain.model.valueobject.PaymentReferenceNumber;
import com.skiply.system.common.domain.model.valueobject.StudentId;
import lombok.Builder;

import java.time.OffsetDateTime;
import java.util.List;

@Builder
public record ReceiptResponse(
   String paidBy,
   StudentInfo studentInfo,
   TransactionDetail transactionDetail,
   PurchaseDetail purchaseDetail
) {
    @Builder
    public record StudentInfo(
            StudentId studentId,
            String name,
            String Grade
    ) {}

    @Builder
    public record TransactionDetail(
            PaymentReferenceNumber referenceNumber,
            OffsetDateTime datetime,
            String cardNumber,
            String cardType
    ) {}

    @Builder
    public record PurchaseDetail(
            Money totalPrice,
            List<PurchaseItem> purchaseItems
    ) {
        @Builder
        public record PurchaseItem(
                String type,
                String name,
                Integer quantity,
                Money price
        ) {}
    }
}
