package com.skiply.system.payment.infrastructure.messaging.publisher;

import com.skiply.system.common.messaging.kafka.message.payment.PaymentSuccessMessage;
import com.skiply.system.common.messaging.kafka.publisher.KafkaProducer;
import com.skiply.system.payment.domain.event.PaymentSucceedEvent;
import com.skiply.system.payment.infrastructure.messaging.publisher.mapper.PaymentMessageMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

public interface PaymentPublisher {
    void publish(PaymentSucceedEvent event);
}

@Slf4j
@RequiredArgsConstructor
@Component
class KafkaPaymentPublisher implements PaymentPublisher {

    @Value("${payment-service.publisher.kafka.topic.payment-success-response}")
    private String topicName;
    private final KafkaProducer<String, PaymentSuccessMessage> kafkaProducer;
    private final PaymentMessageMapper paymentMessageMapper;

    @Override
    public void publish(final PaymentSucceedEvent event) {

        try {
            kafkaProducer.send(topicName, event.getPaymentTransaction().getId().value().toString(),
                    paymentMessageMapper.paymentSucceedEventToMessage(event));
        } catch (Exception e) {
            log.error("Error while sending PaymentSuccessMessage message" +
                            " to kafka with payment id: {}. Error: {}",
                    event.getPaymentTransaction().getId(), e.getMessage());
        }
    }
}