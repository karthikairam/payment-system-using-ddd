package com.skiply.system.receipt.api.dto.receipt;

import com.skiply.system.common.domain.model.valueobject.Money;
import com.skiply.system.common.domain.model.valueobject.PaymentReferenceNumber;
import com.skiply.system.common.domain.model.valueobject.StudentId;
import com.skiply.system.receipt.domain.model.ReceiptStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.OffsetDateTime;
import java.util.List;

@Builder
public record ReceiptResponse(
        @Schema(defaultValue = "User1",
                description = "User who made the payment for the school fee such as Parents/Guardian.",
                example = "Karthik"
        )
        String paidBy,
        @Schema(description = "Student information for whom the fee was paid to.")
        StudentInfo studentInfo,

        @Schema(description = "Payment transaction details for the fee payment.")
        TransactionDetail transactionDetail,
        @Schema(description = "Purchased fees and its detail to be shown in the receipt.")
        PurchaseDetail purchaseDetail,

        @Schema(description = "Purchased fees and its detail to be shown in the receipt.")
        ReceiptStatus receiptStatus
) {
    @Builder
    public record StudentInfo(
            @Schema(type = "string",
                    example = "20220102",
                    description = "Student Id number assigned to him/her in their schools.",
                    maxLength = 20,
                    minLength = 1
            )
            StudentId studentId,
            @Schema(description = "Student's full name (first-name middle-name last-name).")
            String name,
            @Schema(description = "Student's grade in the school at the time of paying this fees.")
            String grade
    ) {
    }

    @Builder
    public record TransactionDetail(
            @Schema(type = "string",
                    example = "1673232016382360789",
                    description = "Payment reference number for the payment made.",
                    maxLength = 30,
                    minLength = 15
            )
            PaymentReferenceNumber paymentReferenceNumber,
            @Schema(example = "2023-01-09T06:40:16.369882+04:00",
                    description = "To maintain the idempotency while submitting the payment",
                    format = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"
            )
            OffsetDateTime datetime,
            @Schema(type = "string",
                    example = "5402192817932256",
                    description = "Card number used for the payment."
            )
            String cardNumber,
            @Schema(type = "string",
                    example = "MC",
                    description = "Type of the card issuer. It can be any of MC,VI,AE & DI."
            )
            String cardType
    ) {
    }

    @Builder
    public record PurchaseDetail(
            @Schema(type = "number",
                    example = "151.5",
                    description = "Total amount paid in this transaction.",
                    format = "double"
            )
            Money totalPrice,
            @Schema(description = "List of purchased fee details.")
            List<PurchaseItem> purchaseItems
    ) {
        @Builder
        public record PurchaseItem(
                String type,
                String name,
                Integer quantity,
                @Schema(type = "number",
                        example = "50.5",
                        description = "Price of individual items.",
                        format = "double"
                )
                Money price
        ) {
        }
    }
}
