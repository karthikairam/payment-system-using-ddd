package com.skiply.system.receipt.infrastructure.persistence.repository;

import com.skiply.system.common.domain.model.valueobject.PaymentReferenceNumber;
import com.skiply.system.receipt.infrastructure.persistence.entity.ReceiptEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ReceiptEntityRepository extends JpaRepository<ReceiptEntity, UUID> {
    ReceiptEntity findByReferenceNumber(PaymentReferenceNumber referenceNumber);
}