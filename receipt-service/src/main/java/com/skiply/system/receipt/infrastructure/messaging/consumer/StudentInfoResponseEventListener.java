package com.skiply.system.receipt.infrastructure.messaging.consumer;

import com.skiply.system.common.messaging.kafka.consumer.KafkaConsumer;
import com.skiply.system.common.messaging.kafka.message.payment.PaymentSuccessMessage;
import com.skiply.system.common.messaging.kafka.message.student.StudentInfoResponseMessage;
import com.skiply.system.receipt.domain.model.ReceiptStatus;
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

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class StudentInfoResponseEventListener implements KafkaConsumer<String, StudentInfoResponseMessage> {

    private final ReceiptEntityRepository repository;

    @Override
    @Transactional
    @KafkaListener(id = "${receipt-service.consumer.kafka.consumer-group-id}",
            topics = "${receipt-service.consumer.kafka.topic.student-info-response}")
    public void receive(@Payload StudentInfoResponseMessage message,
                        @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
                        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) Integer partition,
                        @Header(KafkaHeaders.OFFSET) Long offset) {
        log.info("A student info response message is received with key:{}, partition:{} and offset: {}", key, partition,
                offset);
        try {
            repository.updateStudentInfoById(message.receiptId().value(),
                    message.name(),
                    message.grade(),
                    ReceiptStatus.COMPLETED);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }
}
