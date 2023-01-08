package com.skiply.system.receipt.infrastructure.persistence.entity;

import com.skiply.system.common.domain.model.valueobject.PaymentReferenceNumber;
import com.skiply.system.common.domain.model.valueobject.StudentId;
import com.skiply.system.common.persistence.converter.PaymentReferenceNumberRepoConverter;
import com.skiply.system.common.persistence.converter.StudentIdRepoConverter;
import com.skiply.system.receipt.domain.model.ReceiptStatus;
import lombok.*;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "receipts")
@Entity
public class ReceiptEntity {
    @Id
    @Column(name = "id")
    private UUID id;
    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "transaction_ts")
    private OffsetDateTime transactionTs;

    @Column(name = "paid_by")
    private String paidBy;
    @Column(name = "reference_number")
    @Convert(converter = PaymentReferenceNumberRepoConverter.class)
    private PaymentReferenceNumber referenceNumber;
    @Column(name = "card_number")
    private String cardNumber;
    @Column(name = "card_type")
    private String cardType;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ReceiptStatus status;

    // Due to time constraint I haven't moved the student info required for receipt into another table here.
    @Column(name = "student_id")
    @Convert(converter = StudentIdRepoConverter.class)
    private StudentId studentId;
    @Column(name = "student_name")
    private String studentName;
    @Column(name = "student_grade")
    private String studentGrade;

    @OneToMany(mappedBy = "receipt", cascade = CascadeType.ALL)
    private List<PurchaseItemEntity> purchaseItems;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReceiptEntity receiptEntity = (ReceiptEntity) o;
        return id.equals(receiptEntity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
