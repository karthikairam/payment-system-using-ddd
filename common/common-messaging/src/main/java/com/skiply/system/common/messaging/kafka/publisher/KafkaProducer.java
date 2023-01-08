package com.skiply.system.common.messaging.kafka.publisher;

import com.skiply.system.common.messaging.kafka.message.KafkaMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import javax.annotation.PreDestroy;

public interface KafkaProducer<K extends String, V extends KafkaMessage> {
    void send(String topicName, K key, V message);

    void send(String topicName, K key, V message, ListenableFutureCallback<SendResult<K, V>> callback);
}

@Slf4j
@Component
class KafkaProducerImpl<K extends String, V extends KafkaMessage> implements KafkaProducer<K, V> {

    private final KafkaTemplate<K, V> kafkaTemplate;

    public KafkaProducerImpl(KafkaTemplate<K, V> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void send(String topicName, K key, V message) {
        send(topicName, key, message, null);
    }

    @Override
    public void send(String topicName, K key, V message, ListenableFutureCallback<SendResult<K, V>> callback) {
        log.info("Sending message={} to topic={}", message, topicName);
        try {
            ListenableFuture<SendResult<K, V>> kafkaResultFuture = kafkaTemplate.send(topicName, key, message);
            if(callback != null) {
                kafkaResultFuture.addCallback(callback);
            }
        } catch (KafkaException e) {
            log.error("Error in kafka producer with key: {}, message: {} and exception: {}", key, message,
                    e.getMessage());
            throw e;
        }
    }

    @PreDestroy
    public void destroy() {
        if (kafkaTemplate != null) {
            kafkaTemplate.destroy();
        }
    }
}