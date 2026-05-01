package kg.zhaparov.auditsystem.domain;

import kg.zhaparov.auditsystem.api.PaymentAuditDto;
import kg.zhaparov.auditsystem.domain.db.PaymentAuditLogEntity;
import kg.zhaparov.auditsystem.domain.db.PaymentAuditLogRepository;
import kg.zhaparov.auditsystem.domain.db.ProcessedEventRepository;
import kg.zhaparov.common.events.PaymentEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AuditService {

    private static final Logger logger = LoggerFactory.getLogger(AuditService.class);

    private final PaymentAuditLogRepository auditLogRepository;
    private final ProcessedEventRepository processedEventRepository;

    public AuditService(
            PaymentAuditLogRepository auditLogRepository,
            ProcessedEventRepository processedEventRepository
    ) {
        this.auditLogRepository = auditLogRepository;
        this.processedEventRepository = processedEventRepository;
    }

    @Transactional
    public void processEvent(PaymentEvent event) {
        var isEventAlreadyProcessed = checkIsEventProcessed(event);

        if (isEventAlreadyProcessed) {
            logger.info("Event already processed, skipping");
            return;
        }

        PaymentAuditLogEntity logEntry = convertEventToLogEntity(event);
        auditLogRepository.save(logEntry);
        logger.info("Stored audit log for payment {}", event.paymentId());
    }

    private boolean checkIsEventProcessed(PaymentEvent event) {
        int insertedCount = processedEventRepository.insertIfNotExists(event.eventId());
        return insertedCount == 0;
    }

    @Transactional(readOnly = true)
    public List<PaymentAuditDto> getByPaymentId(Long paymentId) {
        return auditLogRepository.findByPaymentId(paymentId).stream()
                .map(this::convertToPaymentAuditDto)
                .toList();
    }

    private PaymentAuditDto convertToPaymentAuditDto(PaymentAuditLogEntity entry) {
        return new PaymentAuditDto(
                entry.getEventId(),
                entry.getPaymentId(),
                entry.getEventType(),
                entry.getOccurredAt(),
                entry.getAmount(),
                entry.getUserId(),
                entry.getStatus(),
                entry.getCreatedAt()
        );
    }

    private PaymentAuditLogEntity convertEventToLogEntity(PaymentEvent event) {
        return new PaymentAuditLogEntity(
                event.eventId(),
                event.paymentId(),
                event.type().name(),
                event.occurredAt(),
                event.amount(),
                event.userId(),
                event.status()
        );
    }
}
