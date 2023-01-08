package com.skiply.system.receipt.domain.model.event;

import com.skiply.system.common.domain.model.valueobject.StudentId;
import lombok.Builder;

import java.util.UUID;

@Builder
public record RequestStudentInfoEvent(
        StudentId studentId,
        UUID receiptId
) {}
