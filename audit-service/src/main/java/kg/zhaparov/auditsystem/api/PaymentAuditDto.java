package kg.zhaparov.auditsystem.api;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PaymentAuditDto(
        UUID eventId,
        Long paymentId,
        String eventType,
        LocalDateTime occurredAt,
        BigDecimal amount,
        Long userId,
        String status,
        LocalDateTime createdAt
) {
}

