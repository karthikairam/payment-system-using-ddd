package com.skiply.system.common.messaging.kafka.message.student;

import com.skiply.system.common.domain.model.valueobject.StudentId;
import com.skiply.system.common.messaging.kafka.message.KafkaMessage;
import lombok.Builder;

@Builder
public record StudentInfoResponseMessage(
        StudentId studentId,
        String name,
        String grade
) implements KafkaMessage {}
