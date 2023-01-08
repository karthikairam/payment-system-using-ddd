package com.skiply.system.receipt.infrastructure.messaging.publisher;

import com.skiply.system.common.messaging.kafka.message.receipt.StudentInfoRequestMessage;
import com.skiply.system.common.messaging.kafka.publisher.KafkaProducer;
import com.skiply.system.receipt.domain.model.event.RequestStudentInfoEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

public interface RequestStudentInfoEventPublisher {
    void publish(RequestStudentInfoEvent event);
}

@Slf4j
@RequiredArgsConstructor
@Component
class KafkaRequestStudentInfoEventPublisher implements RequestStudentInfoEventPublisher {

    @Value("${receipt-service.publisher.kafka.topic.student-info-request}")
    private String topicName;
    private final KafkaProducer<String, StudentInfoRequestMessage> kafkaProducer;

    @Override
    public void publish(final RequestStudentInfoEvent event) {

        try {
            kafkaProducer.send(topicName, event.studentId().value(), new StudentInfoRequestMessage(event.receiptId()));
        } catch (Exception e) {
            log.error("Error while sending StudentInfoRequestMessage message" +
                            " to kafka with receipt id: {}. Error: {}",
                    event.receiptId(), e.getMessage());
        }
    }
}