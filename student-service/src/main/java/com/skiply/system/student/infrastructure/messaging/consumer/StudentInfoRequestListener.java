package com.skiply.system.student.infrastructure.messaging.consumer;

import com.skiply.system.common.messaging.kafka.consumer.KafkaConsumer;
import com.skiply.system.common.messaging.kafka.message.receipt.StudentInfoRequestMessage;
import com.skiply.system.student.domain.model.event.StudentInfoResponseEvent;
import com.skiply.system.student.infrastructure.messaging.publisher.ResponseStudentInfoEventPublisher;
import com.skiply.system.student.infrastructure.persistence.mapper.StudentEntityMapper;
import com.skiply.system.student.infrastructure.persistence.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StudentInfoRequestListener implements KafkaConsumer<String, StudentInfoRequestMessage> {

    private final StudentRepository repository;

    private final ResponseStudentInfoEventPublisher responseStudentInfoEventPublisher;

    private final StudentEntityMapper studentEntityMapper;

    @Override
    @KafkaListener(topics = "${student-service.consumer.kafka.topic.student-info-request}")
    public void receive(@Payload StudentInfoRequestMessage message,
                        @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
                        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) Integer partition,
                        @Header(KafkaHeaders.OFFSET) Long offset) {
        log.info("A payment success message is received with key:{}, partition:{} and offset: {}", key, partition,
                offset);
        try {
            var entity = repository.findById(key);
            if(entity.isEmpty() || entity.get().isActive()) {
                log.warn("Requested student id: {} is either not found or inactive in the system", key);
                return;
            }
            responseStudentInfoEventPublisher.publish(StudentInfoResponseEvent.builder()
                    .receiptId(message.receiptId())
                    .student(studentEntityMapper.entityToStudentModel(entity.get()))
                    .build());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }
}
