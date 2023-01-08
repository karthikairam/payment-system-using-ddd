package com.skiply.system.kafka.server;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.EmbeddedKafkaBroker;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@SpringBootApplication
public class KafkaServerApplication {
	public static void main(String[] args) {
		SpringApplication.run(KafkaServerApplication.class, args);
	}

	@Value(value = "${spring.kafka.bootstrap-servers}")
	private String bootstrapAddress;

	@Bean
	EmbeddedKafkaBroker broker() {
		return new EmbeddedKafkaBroker(1)
				.kafkaPorts(10091)
				.brokerListProperty("spring.kafka.bootstrap-servers");
	}

	@Bean
	public KafkaAdmin kafkaAdmin() {
		Map<String, Object> configs = new HashMap<>();
		configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
		return new KafkaAdmin(configs);
	}

	@Bean
	public NewTopic topic1() {
		return TopicBuilder.name("test-topic-1989").partitions(1).replicas(1).build();
	}

	@Bean
	public NewTopic topicPaymentSuccessEvent() {
		return TopicBuilder.name("payment-success-response").partitions(1).replicas(1).build();
	}

	@KafkaListener(id = "test-topic-1989", topics = "test-topic-1989")
	public void listen(String in) {
		log.info("Message Received: {}", in);
	}

	@Bean
	public ApplicationRunner runner(KafkaTemplate<String, String> template) {
		return args -> template.send("test-topic-1989", "Hey, Kafka! Welcome to my localhost.");
	}

}
