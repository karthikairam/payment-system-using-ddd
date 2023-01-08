package com.skiply.system.common.messaging.kafka.message.receipt;

import com.skiply.system.common.messaging.kafka.message.KafkaMessage;
import lombok.Builder;

import java.util.UUID;

@Builder
public record StudentInfoRequestMessage(UUID receiptId) implements KafkaMessage {}
