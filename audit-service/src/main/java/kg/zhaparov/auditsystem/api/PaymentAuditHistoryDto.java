package kg.zhaparov.auditsystem.api;

import java.util.List;

public record PaymentAuditHistoryDto(
        List<PaymentAuditDto> events
) {
}
