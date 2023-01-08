package com.skiply.system.payment.infrastructure.persistence.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "idempotency_keys")
public class IdempotencyKeyEntity {
    @Id
    @Column(name = "idempotency_key", nullable = false)
    private String key;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "payment_transaction_id")
    private PaymentTransactionEntity paymentTransaction;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IdempotencyKeyEntity that = (IdempotencyKeyEntity) o;
        return key.equals(that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }
}
