package kg.zhaparov.common.events;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PaymentEvent(
        UUID eventId,
        PaymentEventType type,
        Long paymentId,
        LocalDateTime occurredAt,
        BigDecimal amount,
        Long userId,
        String status
) {
}
