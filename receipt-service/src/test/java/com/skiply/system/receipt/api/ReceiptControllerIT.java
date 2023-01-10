package com.skiply.system.receipt.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skiply.system.common.domain.model.valueobject.Money;
import com.skiply.system.common.domain.model.valueobject.PaymentReferenceNumber;
import com.skiply.system.common.domain.model.valueobject.ReceiptId;
import com.skiply.system.common.domain.model.valueobject.StudentId;
import com.skiply.system.common.messaging.kafka.message.KafkaMessage;
import com.skiply.system.common.messaging.kafka.message.payment.PaymentSuccessMessage;
import com.skiply.system.common.messaging.kafka.message.receipt.StudentInfoRequestMessage;
import com.skiply.system.common.messaging.kafka.message.student.StudentInfoResponseMessage;
import com.skiply.system.receipt.TestKafkaServerConfiguration;
import com.skiply.system.receipt.domain.model.ReceiptStatus;
import com.skiply.system.receipt.infrastructure.persistence.entity.ReceiptEntity;
import com.skiply.system.receipt.infrastructure.persistence.repository.ReceiptEntityRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    private KafkaTemplate<String, KafkaMessage> kafkaTemplate;

    @Autowired
    private ConsumerFactory<String, PaymentSuccessMessage> consumerFactory;

    private KafkaMessageListenerContainer<String, PaymentSuccessMessage> container;
    private BlockingQueue<ConsumerRecord<String, StudentInfoRequestMessage>> queue;

    @BeforeEach
    void setup() {
        queue = new LinkedBlockingQueue<>();
        ContainerProperties containerProperties = new ContainerProperties("student-info-request");
        container = new KafkaMessageListenerContainer<>(consumerFactory, containerProperties);
        container.setupMessageListener((MessageListener<String, StudentInfoRequestMessage>) queue::add);
        container.start();
        ContainerTestUtils.waitForAssignment(container, 1);
    }

    @AfterEach
    void cleanUp() {
        container.stop();
        repository.deleteAll();
    }

    @Test
    void givenSuccessfulPaymentWhenPaymentEventAndStudentInfoEventReceivedThenSystemRespondsCompleted()
            throws Exception {

        var studentId = new StudentId("89898989");
        var paymentReferenceNumber = new PaymentReferenceNumber("908070605040302010");

        //simulate publishing a payment event
        simulatePublishingPaymentSuccessMessageEvent(studentId, paymentReferenceNumber);

        //validate whether student-info-request event is published
        awaitUntil(() -> repository.existsByPaymentReferenceNumber(paymentReferenceNumber));
        var receiptEntity = repository.findByPaymentReferenceNumber(paymentReferenceNumber);
        assertThat(receiptEntity).isNotNull();
        assertThat(receiptEntity.getId()).isNotNull();

        var studentInfoRequestEventRecord = queue.poll(100, TimeUnit.MILLISECONDS);
        assertThat(studentInfoRequestEventRecord).isNotNull();
        assertThat(studentInfoRequestEventRecord.key()).isEqualTo(studentId.value());
        assertThat(studentInfoRequestEventRecord.value().receiptId().value()).isEqualTo(receiptEntity.getId());

        //simulate publishing a student info event with same student id as above
        simulatePublishingStudentInfoResponseEvent(studentId, receiptEntity);

        //validate receipt endpoint call
        awaitUntil(() -> repository
                        .findByPaymentReferenceNumber(paymentReferenceNumber)
                        .getStatus() == ReceiptStatus.COMPLETED);

        fireGetReceiptApi(paymentReferenceNumber, status().isOk(), studentId, ReceiptStatus.COMPLETED);

        //validate DB for receipt record
        var finalReceiptEntity = repository.findByPaymentReferenceNumber(paymentReferenceNumber);
        assertThat(finalReceiptEntity).isNotNull();
        assertThat(finalReceiptEntity.getId()).isNotNull();
        assertThat(finalReceiptEntity.getStatus()).isEqualTo(ReceiptStatus.COMPLETED);
    }

    @Test
    void givenSuccessfulPaymentWhenPaymentEventReceivedAndStudentInfoEventNotReceivedThenSystemRespondsPending()
            throws Exception {

        var studentId = new StudentId("89898989");
        var paymentReferenceNumber = new PaymentReferenceNumber("908070605040302010");

        //simulate publishing a payment event
        simulatePublishingPaymentSuccessMessageEvent(studentId, paymentReferenceNumber);

        //validate whether student-info-request event is published
        awaitUntil(() -> repository.existsByPaymentReferenceNumber(paymentReferenceNumber));
        var receiptEntity = repository.findByPaymentReferenceNumber(paymentReferenceNumber);
        assertThat(receiptEntity).isNotNull();
        assertThat(receiptEntity.getId()).isNotNull();

        var studentInfoRequestEventRecord = queue.poll(100, TimeUnit.MILLISECONDS);
        assertThat(studentInfoRequestEventRecord).isNotNull();
        assertThat(studentInfoRequestEventRecord.key()).isEqualTo(studentId.value());
        assertThat(studentInfoRequestEventRecord.value().receiptId().value()).isEqualTo(receiptEntity.getId());

        //Before student information is updated, the status of receipt has to be PENDING
        fireGetReceiptApi(paymentReferenceNumber, status().isAccepted(), studentId, ReceiptStatus.PENDING);
    }

    private void awaitUntil(Callable<Boolean> call) {
        Awaitility.await()
                .atLeast(100, TimeUnit.MILLISECONDS)
                .atMost(5, TimeUnit.SECONDS)
                .pollInterval(100, TimeUnit.MILLISECONDS)
                .until(call);
    }

    private void fireGetReceiptApi(PaymentReferenceNumber paymentReferenceNumber, ResultMatcher Accepted, StudentId studentId, ReceiptStatus expectedStatus) throws Exception {
        mockMvc.perform(get("/v1/receipts?paymentReferenceNumber={value}", paymentReferenceNumber.value())
                        .accept("application/json")
                )
                .andExpect(Accepted)
                .andExpectAll(
                        jsonPath("$.studentInfo.studentId")
                                .value(studentId.value()),
                        jsonPath("$.transactionDetail.paymentReferenceNumber")
                                .value(paymentReferenceNumber.value()),
                        jsonPath("$.receiptStatus").value(expectedStatus.name())
                )
        ;
    }

    private void simulatePublishingStudentInfoResponseEvent(StudentId studentId, ReceiptEntity receiptEntity) {
        var studentInfoResponseEvent = StudentInfoResponseMessage.builder()
                .grade("KG1")
                .studentId(studentId)
                .name("Test Student")
                .receiptId(new ReceiptId(receiptEntity.getId()))
                .build();
        kafkaTemplate.send("student-info-response", studentId.value(), studentInfoResponseEvent);
    }

    private void simulatePublishingPaymentSuccessMessageEvent(
            StudentId studentId, PaymentReferenceNumber paymentReferenceNumber) {

        var paymentSuccessResponseEvent = PaymentSuccessMessage.builder()
                .paidBy("User")
                .studentId(studentId)
                .purchaseItems(
                        List.of(
                                PaymentSuccessMessage.PurchaseItem.builder()
                                        .quantity(3)
                                        .feeType("Tuition")
                                        .name("KG1")
                                        .price(new Money(new BigDecimal("50.00")))
                                        .build()
                        )
                )
                .transactionDetail(PaymentSuccessMessage.TransactionDetail.builder()
                        .transactionDateTime(OffsetDateTime.now())
                        .paymentReferenceNumber(paymentReferenceNumber)
                        .cardType("MC")
                        .cardNumber("5432678934561234")
                        .build())
                .build();
        kafkaTemplate.send("payment-success-response", paymentReferenceNumber.value(), paymentSuccessResponseEvent);
    }
}