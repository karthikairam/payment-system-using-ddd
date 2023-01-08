package com.skiply.system.common.messaging.kafka.message.receipt;

import com.skiply.system.common.domain.model.valueobject.ReceiptId;
import com.skiply.system.common.messaging.kafka.message.KafkaMessage;
import lombok.Builder;

@Builder
public record StudentInfoRequestMessage(ReceiptId receiptId) implements KafkaMessage {}
