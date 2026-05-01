package kg.zhaparov.auditsystem.domain.db;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payment_audit_log")
@Getter
@Setter
public class PaymentAuditLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_id", nullable = false)
    private UUID eventId;

    @Column(name = "payment_id", nullable = false)
    private Long paymentId;

    @Column(name = "event_type", nullable = false, length = 64)
    private String eventType;

    @Column(name = "occurred_at", nullable = false)
    private LocalDateTime occurredAt;

    @Column(name = "amount", precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "status", length = 32)
    private String status;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public PaymentAuditLogEntity() {
    }

    public PaymentAuditLogEntity(UUID eventId, Long paymentId, String eventType, LocalDateTime occurredAt,
                                 BigDecimal amount, Long userId, String status) {
        this.eventId = eventId;
        this.paymentId = paymentId;
        this.eventType = eventType;
        this.occurredAt = occurredAt;
        this.amount = amount;
        this.userId = userId;
        this.status = status;
    }
}
