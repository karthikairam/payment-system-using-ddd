package com.skiply.system.student.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skiply.system.common.domain.model.valueobject.MobileNumber;
import com.skiply.system.common.domain.model.valueobject.ReceiptId;
import com.skiply.system.common.domain.model.valueobject.StudentId;
import com.skiply.system.common.messaging.kafka.message.KafkaMessage;
import com.skiply.system.common.messaging.kafka.message.receipt.StudentInfoRequestMessage;
import com.skiply.system.common.messaging.kafka.message.student.StudentInfoResponseMessage;
import com.skiply.system.student.TestKafkaServerConfiguration;
import com.skiply.system.student.api.register.RegisterStudentCommand;
import com.skiply.system.student.infrastructure.persistence.repository.StudentRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles({"test"})
class StudentControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private StudentRepository repository;

    @AfterEach
    void cleanUp() {
        repository.deleteAll();
    }

    @Test
    void givenValidRegistrationRequestThenSystemRegistersTheStudent() throws Exception {
        var request = RegisterStudentCommand.builder()
                .studentId(new StudentId("98989899"))
                .grade("KG1")
                .mobileNumber(new MobileNumber("+971555555555"))
                .studentName("FirstName MiddleName LastName")
                .schoolName("School Name")
                .build();

        mockMvc.perform(
                        post("/v1/students")
                                .contentType("application/json")
                                .accept("application/json")
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
        ;

        var entity = repository.findById(request.studentId().value());
        entity.ifPresentOrElse(studentEntity -> {
            assertThat(studentEntity.getStudentId()).isEqualTo(request.studentId().value());
            assertThat(studentEntity.getStudentName()).isEqualTo(request.studentName());
            assertThat(studentEntity.getSchoolName()).isEqualTo(request.schoolName());
            assertThat(studentEntity.getMobileNumber()).isEqualTo(request.mobileNumber());
            assertThat(studentEntity.isActive()).isTrue();
        }, () -> fail("Entity has not saved."));
    }

    @Test
    void testIdempotencyWhileStudentRegistration() throws Exception {
        var request = RegisterStudentCommand.builder()
                .studentId(new StudentId("98989899"))
                .grade("KG1")
                .mobileNumber(new MobileNumber("+971555555555"))
                .studentName("FirstName MiddleName LastName")
                .schoolName("School Name")
                .build();

        //First call
        mockMvc.perform(
                        post("/v1/students")
                                .contentType("application/json")
                                .accept("application/json")
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
        ;

        // Second call also expected to 201 because implicitly server side we maintain idempotency
        // and letting the client know that it was created already. So, there won't be two records created!
        mockMvc.perform(
                        post("/v1/students")
                                .contentType("application/json")
                                .accept("application/json")
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
        ;

        //Validating record created & only one created. If two records found following will throw error.
        var entity = repository.findById(request.studentId().value());
        assertThat(entity.isPresent()).isTrue();
    }

    @Nested
    @EmbeddedKafka
    @Import(TestKafkaServerConfiguration.class)
    class KafkaBasedTestCases {

        @Autowired
        private KafkaTemplate<String, KafkaMessage> kafkaTemplate;

        @Autowired
        private ConsumerFactory<String, StudentInfoResponseMessage> consumerFactory;

        private KafkaMessageListenerContainer<String, StudentInfoResponseMessage> container;
        private BlockingQueue<ConsumerRecord<String, StudentInfoResponseMessage>> queue;

        @BeforeEach
        void setup() {
            queue = new LinkedBlockingQueue<>();
            ContainerProperties containerProperties = new ContainerProperties("student-info-response");
            container = new KafkaMessageListenerContainer<>(consumerFactory, containerProperties);
            container.setupMessageListener((MessageListener<String, StudentInfoResponseMessage>) queue::add);
            container.start();
            ContainerTestUtils.waitForAssignment(container, 1);
        }

        @AfterEach
        void cleanUp() {
            container.stop();
        }

        @Test
        void testStudentInfoRequestAndResponseEventModel() throws Exception {
            var studentId = new StudentId("89898989");
            var receiptId = new ReceiptId(UUID.randomUUID());

            // Create a student
            var request = RegisterStudentCommand.builder()
                    .studentId(studentId)
                    .grade("KG1")
                    .mobileNumber(new MobileNumber("+971555555555"))
                    .studentName("FirstName MiddleName LastName")
                    .schoolName("School Name")
                    .build();

            mockMvc.perform(
                            post("/v1/students")
                                    .contentType("application/json")
                                    .accept("application/json")
                                    .content(objectMapper.writeValueAsString(request))
                    )
                    .andExpect(status().isCreated())
            ;

            //Simulate student request event
            kafkaTemplate.send("student-info-request", studentId.value(), new StudentInfoRequestMessage(receiptId));

            Awaitility.await()
                    .atMost(2, TimeUnit.SECONDS)
                    .pollInterval(50, TimeUnit.MILLISECONDS)
                    .until(() -> queue.size() > 0);

            var studentResponseMessageRecord = queue.poll();
            assertThat(studentResponseMessageRecord).isNotNull();
            assertThat(studentResponseMessageRecord.key()).isEqualTo(receiptId.value().toString());
            assertThat(studentResponseMessageRecord.value().studentId()).isEqualTo(studentId);
        }
    }


    @Test
    void badRequestWhileStudentRegistration() throws Exception {
        var request = RegisterStudentCommand.builder()
                .studentId(new StudentId("98989899"))
                .grade("KG1")
                .mobileNumber(new MobileNumber("+971555555555"))
                .studentName("")
                .schoolName("School Name")
                .build();

        //First call
        mockMvc.perform(
                        post("/v1/students")
                                .contentType("application/json")
                                .accept("application/json")
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
        ;

        var entity = repository.findById(request.studentId().value());
        assertThat(entity.isPresent()).isFalse();
    }

}