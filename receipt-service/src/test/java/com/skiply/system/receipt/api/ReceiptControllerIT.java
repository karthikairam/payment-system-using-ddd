package com.skiply.system.receipt.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skiply.system.common.messaging.kafka.message.payment.PaymentSuccessMessage;
import com.skiply.system.receipt.TestKafkaServerConfiguration;
import com.skiply.system.receipt.infrastructure.persistence.repository.ReceiptEntityRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@Import(TestKafkaServerConfiguration.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles({"test"})
@DirtiesContext
@EmbeddedKafka
class ReceiptControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ReceiptEntityRepository repository;

    @Autowired
    private ConsumerFactory<String, PaymentSuccessMessage> consumerFactory;

    private KafkaMessageListenerContainer<String, PaymentSuccessMessage> container;
    private BlockingQueue<ConsumerRecord<String, PaymentSuccessMessage>> queue;

    @BeforeEach
    void setup() {
        queue = new LinkedBlockingQueue<>();
        ContainerProperties containerProperties = new ContainerProperties("payment-success-response");
        container = new KafkaMessageListenerContainer<>(consumerFactory, containerProperties);
        container.setupMessageListener((MessageListener<String, PaymentSuccessMessage>) queue::add);
        container.start();
        ContainerTestUtils.waitForAssignment(container, 1);
    }

    @AfterEach
    void cleanUp() {
        container.stop();
        repository.deleteAll();
    }
}