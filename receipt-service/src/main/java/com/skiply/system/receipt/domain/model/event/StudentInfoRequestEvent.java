package com.skiply.system.receipt.domain.model.event;

import com.skiply.system.common.domain.model.valueobject.ReceiptId;
import com.skiply.system.common.domain.model.valueobject.StudentId;
import lombok.Builder;

@Builder
public record StudentInfoRequestEvent(
        StudentId studentId,
        ReceiptId receiptId
) {}
