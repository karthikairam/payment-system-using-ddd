package com.skiply.system.receipt.infrastructure.messaging.consumer;

import com.skiply.system.common.messaging.kafka.consumer.KafkaConsumer;
import com.skiply.system.common.messaging.kafka.message.payment.PaymentSuccessMessage;
import com.skiply.system.receipt.infrastructure.messaging.publisher.RequestStudentInfoEventPublisher;
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

    private final RequestStudentInfoEventPublisher requestStudentInfoEventPublisher;

    @Override
    @Transactional
    @KafkaListener(id = "${receipt-service.consumer.kafka.consumer-group-id}",
            topics = "${receipt-service.consumer.kafka.topic.payment-success-response}")
    public void receive(@Payload PaymentSuccessMessage message,
                        @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
                        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) Integer partition,
                        @Header(KafkaHeaders.OFFSET) Long offset) {
        log.info("A payment success message is received with key:{}, partition:{} and offset: {}", key, partition,
                offset);
        try {
            var entity = repository.save(dataMapper.messageToEntity(message));
            // 1. publish request-student-info-topic
            // 2. Write another listener to listen for student information
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }
}
