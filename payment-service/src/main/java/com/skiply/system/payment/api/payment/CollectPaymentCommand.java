package com.skiply.system.payment.api.payment;

import com.skiply.system.common.domain.model.valueobject.StudentId;
import com.skiply.system.payment.domain.model.IdempotencyKey;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.List;

public record CollectPaymentCommand(
        @Schema(type = "string", defaultValue = "9898989")
        @NotBlank
        StudentId studentId, // Value object is validated
        @NotBlank
        String paidBy,
        @Schema(type = "string",
                defaultValue = "20220102123520234",
                description = "To maintain idempotency while submitting the payment",
                maxLength = 20,
                minLength = 10
        )
        @NotBlank
        IdempotencyKey idempotencyKey,
        @NotBlank
        String cardNumber,
        @NotBlank
        String cardType,
        @Schema(type = "number", defaultValue = "0.00", format = "decimal", example = "150.00")
        @NotBlank
        BigDecimal totalFeePaid,
        @NotBlank
        List<PurchaseItem> purchaseItems
) {
    public record PurchaseItem(
            String feeType,
            String name,
            Integer quantity,
            BigDecimal price
    ) {
    }
}
