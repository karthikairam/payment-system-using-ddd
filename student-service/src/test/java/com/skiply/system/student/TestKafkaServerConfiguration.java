package com.skiply.system.student;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.test.EmbeddedKafkaBroker;

import java.util.HashMap;
import java.util.Map;

@TestConfiguration
public class TestKafkaServerConfiguration {

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Bean
    EmbeddedKafkaBroker broker() {
        return new EmbeddedKafkaBroker(1)
                .kafkaPorts(10092)
                .brokerListProperty("spring.kafka.bootstrap-servers");
    }

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic topicStudentInfoRequest() {
        return createNewTopicWithDefaults("student-info-request");
    }

    @Bean
    public NewTopic topicStudentInfoResponse() {
        return createNewTopicWithDefaults("student-info-response");
    }

    private NewTopic createNewTopicWithDefaults(String name) {
        return TopicBuilder.name(name)
                .partitions(1)
                .replicas(1)
                .build();
    }
}
