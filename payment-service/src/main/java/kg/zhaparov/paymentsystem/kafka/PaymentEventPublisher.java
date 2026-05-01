package kg.zhaparov.paymentsystem.kafka;

import kg.zhaparov.common.events.PaymentEvent;
import kg.zhaparov.common.events.PaymentEventType;
import kg.zhaparov.paymentsystem.domain.db.PaymentEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class PaymentEventPublisher {

    private static final Logger logger = LoggerFactory.getLogger(PaymentEventPublisher.class);

    private final KafkaTemplate<String, PaymentEvent> kafkaTemplate;
    private final String topic;

    public PaymentEventPublisher(
            KafkaTemplate<String, PaymentEvent> kafkaTemplate,
            @Value("${app.kafka.topics.payments}") String topic
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    public void publishPaymentCreated(PaymentEntity payment) {
        publish(payment, PaymentEventType.PAYMENT_CREATED);
    }

    public void publishPaymentSucceeded(PaymentEntity payment) {
        publish(payment, PaymentEventType.PAYMENT_SUCCEEDED);
    }

    private void publish(
            PaymentEntity payment,
            PaymentEventType type
    ) {
        PaymentEvent event = new PaymentEvent(
                UUID.randomUUID(),
                type,
                payment.getId(),
                LocalDateTime.now(),
                payment.getAmount(),
                payment.getUserId(),
                payment.getStatus().name()
        );
        kafkaTemplate.send(topic, payment.getId().toString(), event);
        logger.info("Published event {} for payment {}", type, payment.getId());
    }
}