package com.skiply.system.receipt.infrastructure.messaging.consumer;

import com.skiply.system.common.domain.model.valueobject.ReceiptId;
import com.skiply.system.common.messaging.kafka.consumer.KafkaConsumer;
import com.skiply.system.common.messaging.kafka.message.payment.PaymentSuccessMessage;
import com.skiply.system.receipt.domain.model.event.StudentInfoRequestEvent;
import com.skiply.system.receipt.infrastructure.messaging.publisher.StudentInfoRequestEventPublisher;
import com.skiply.system.receipt.infrastructure.persistence.repository.ReceiptEntityRepository;
import com.skiply.system.receipt.service.mapper.ReceiptDataMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentSuccessEventListener implements KafkaConsumer<String, PaymentSuccessMessage> {

    private final ReceiptEntityRepository repository;

    private final ReceiptDataMapper dataMapper;

    private final StudentInfoRequestEventPublisher studentInfoRequestEventPublisher;

    @Override
    @Transactional
    @KafkaListener(topics = "${receipt-service.consumer.kafka.topic.payment-success-response}")
    public void receive(@Payload PaymentSuccessMessage message,
                        @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
                        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) Integer partition,
                        @Header(KafkaHeaders.OFFSET) Long offset) {
        log.info("A payment success message is received with key:{}, partition:{} and offset: {}", key, partition,
                offset);
        try {
            var entity = repository.save(dataMapper.messageToEntity(message));
            studentInfoRequestEventPublisher.publish(StudentInfoRequestEvent.builder()
                    .receiptId(new ReceiptId(entity.getId()))
                    .studentId(entity.getStudentId())
                    .build());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }
}
