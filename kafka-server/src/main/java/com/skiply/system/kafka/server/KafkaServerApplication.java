package com.skiply.system.kafka.server;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.test.EmbeddedKafkaBroker;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@SpringBootApplication
public class KafkaServerApplication {
	public static void main(String[] args) {
		SpringApplication.run(KafkaServerApplication.class, args);
	}
}

@Configuration
class KafkaServerConfiguration {

	@Value(value = "${spring.kafka.bootstrap-servers}")
	private String bootstrapAddress;

	@Bean
	EmbeddedKafkaBroker broker() {
		return new EmbeddedKafkaBroker(1)
				.kafkaPorts(10091)
				.brokerListProperty("spring.kafka.bootstrap-servers");
	}

	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper()
				.registerModule(new Jdk8Module())
				.registerModule(new JavaTimeModule())
				.setSerializationInclusion(JsonInclude.Include.NON_NULL)
				.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
	}

	@Bean
	public KafkaAdmin kafkaAdmin() {
		Map<String, Object> configs = new HashMap<>();
		configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
		return new KafkaAdmin(configs);
	}

	@Bean
	public NewTopic topicPaymentSuccessEvent() {
		return createNewTopicWithDefaults("payment-success-response");
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
