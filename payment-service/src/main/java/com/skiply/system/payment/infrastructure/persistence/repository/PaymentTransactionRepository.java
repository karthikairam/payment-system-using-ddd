package com.skiply.system.payment.infrastructure.persistence.repository;

import com.skiply.system.common.domain.model.valueobject.PaymentReferenceNumber;
import com.skiply.system.payment.infrastructure.persistence.entity.PaymentTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentTransactionRepository extends JpaRepository<PaymentTransactionEntity, UUID> {
    Optional<PaymentTransactionEntity> findByPaymentReferenceNumber(PaymentReferenceNumber paymentReferenceNumber);
}
