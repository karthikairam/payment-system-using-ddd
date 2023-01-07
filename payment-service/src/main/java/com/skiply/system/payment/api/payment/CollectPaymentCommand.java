package com.skiply.system.payment.api.payment;

import com.skiply.system.common.domain.model.valueobject.StudentId;
import com.skiply.system.payment.domain.model.IdempotencyKey;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.*;
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
        @Pattern(regexp = "^(MC|VI|AE|DI)$", message = "Invalid card type")
        String cardType,
        @NotBlank
        @Pattern(regexp = "^[0-9]{3,4}$", message = "Invalid CVV number")
        String cardCvv,
        @NotBlank
        @Pattern(regexp = "^(0[1-9]|1[0-2])/[0-9]{2}$", message = "Invalid expiration date")
        String cardExpiry,
        @Schema(type = "number", defaultValue = "0.00", format = "decimal", example = "150.00")
        @NotNull
        @DecimalMax(value = "9999999999.99", message = "Value must be less than or equal to 9999999999.99")
        @DecimalMin(value = "0.00", message = "Value must be greater than or equal to 0.00")
        @Digits(integer = 10, fraction = 2, message = "Value must have a maximum of 10 integer digits and 2 fraction digits")
        BigDecimal totalPrice,
        @NotBlank
        List<PurchaseItem> purchaseItems
) {
    public record PurchaseItem(
            @NotBlank
            String feeType,
            @NotBlank
            String name,
            @NotNull
            @Min(value = 1)
            @Max(value = 50)
            Integer quantity,
            @Schema(type = "number", defaultValue = "0.00", format = "decimal", example = "150.00")
            @NotNull
            @DecimalMax(value = "9999999999.99", message = "Value must be less than or equal to 9999999999.99")
            @DecimalMin(value = "0.00", message = "Value must be greater than or equal to 0.00")
            @Digits(integer = 10, fraction = 2, message = "Value must have a maximum of 10 integer digits and 2 fraction digits")
            BigDecimal price
    ) {
    }
}
