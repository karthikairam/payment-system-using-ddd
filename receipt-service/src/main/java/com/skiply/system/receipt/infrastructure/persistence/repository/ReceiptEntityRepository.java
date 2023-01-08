package com.skiply.system.receipt.infrastructure.persistence.repository;

import com.skiply.system.common.domain.model.valueobject.PaymentReferenceNumber;
import com.skiply.system.receipt.domain.model.ReceiptStatus;
import com.skiply.system.receipt.infrastructure.persistence.entity.ReceiptEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ReceiptEntityRepository extends JpaRepository<ReceiptEntity, UUID> {
    ReceiptEntity findByReferenceNumber(PaymentReferenceNumber referenceNumber);

    @Modifying
    @Query("""
            update ReceiptEntity u
                set
                    u.student_name = :studentName
                    u.student_grade = :studentGrade
                    u.status = :receiptStatus
                where u.id = :id
            """)
    void updateStudentInfoById(@Param(value = "id") UUID id,
                               @Param(value = "studentName") String studentName,
                               @Param(value = "studentGrade") String studentGrade,
                               @Param(value = "receiptStatus") ReceiptStatus receiptStatus
    );
}