package com.skiply.system.student.infrastructure.messaging.publisher;

import com.skiply.system.common.messaging.kafka.message.student.StudentInfoResponseMessage;
import com.skiply.system.common.messaging.kafka.publisher.KafkaProducer;
import com.skiply.system.student.domain.model.event.StudentInfoResponseEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

public interface ResponseStudentInfoEventPublisher {
    void publish(StudentInfoResponseEvent event);
}

@Slf4j
@Component
@RequiredArgsConstructor
class KafkaResponseStudentInfoEventPublisher implements ResponseStudentInfoEventPublisher {

    @Value("${student-service.publisher.kafka.topic.student-info-response}")
    private String topicName;

    private final KafkaProducer<String, StudentInfoResponseMessage> kafkaProducer;

    @Override
    public void publish(StudentInfoResponseEvent event) {
        try {
            kafkaProducer.send(topicName, event.receiptId().toString(), prepareMessage(event));
        } catch (Exception e) {
            log.error("Error while sending StudentInfoResponseMessage message" +
                            " to kafka with student id: {}. Error: {}",
                    event.student().getId(), e.getMessage());
        }
    }

    private StudentInfoResponseMessage prepareMessage(StudentInfoResponseEvent event) {
        return StudentInfoResponseMessage.builder()
                .receiptId(event.receiptId())
                .studentId(event.student().getId())
                .name(event.student().getName())
                .grade(event.student().getGrade())
                .build();
    }
}
