package com.skiply.system.payment.infrastructure.persistence.entity;

import com.skiply.system.common.domain.model.valueobject.PaymentReferenceNumber;
import com.skiply.system.common.domain.model.valueobject.PaymentTransactionStatus;
import com.skiply.system.common.domain.model.valueobject.StudentId;
import com.skiply.system.common.persistence.converter.PaymentReferenceNumberRepoConverter;
import com.skiply.system.common.persistence.converter.StudentIdRepoConverter;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payment_transactions")
@Entity
public class PaymentTransactionEntity {
    @Id
    @Column(name = "id")
    private UUID id;
    @Column(name = "created_at")
    private OffsetDateTime createdAt;
    @Column(name = "student_id")
    @Convert(converter = StudentIdRepoConverter.class)
    private StudentId studentId;
    @Column(name = "paid_by")
    private String paidBy;
    @Column(name = "payment_reference_number")
    @Convert(converter = PaymentReferenceNumberRepoConverter.class)
    private PaymentReferenceNumber paymentReferenceNumber;
    @Column(name = "card_number")
    private String cardNumber;
    @Column(name = "card_type")
    private String cardType;
    @Column(name = "total_fee_paid")
    private BigDecimal totalFeePaid;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PaymentTransactionStatus status;

    @OneToOne(mappedBy = "paymentTransaction", cascade = CascadeType.ALL)
    private IdempotencyKeyEntity idempotencyKey;

    @OneToMany(mappedBy = "paymentTransaction", cascade = CascadeType.ALL)
    private List<PurchaseItemEntity> purchaseItems;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaymentTransactionEntity that = (PaymentTransactionEntity) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
