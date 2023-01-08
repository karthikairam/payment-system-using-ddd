package com.skiply.system.student.domain.model.event;

import com.skiply.system.common.domain.event.DomainEvent;
import com.skiply.system.common.domain.model.valueobject.ReceiptId;
import com.skiply.system.student.domain.model.Student;
import lombok.Builder;

@Builder
public record StudentInfoResponseEvent(
        ReceiptId receiptId,
        Student student
) implements DomainEvent<Student> {}
