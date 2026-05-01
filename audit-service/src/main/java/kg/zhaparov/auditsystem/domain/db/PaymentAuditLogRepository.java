package kg.zhaparov.auditsystem.domain.db;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentAuditLogRepository extends JpaRepository<PaymentAuditLogEntity, Long> {
    List<PaymentAuditLogEntity> findByPaymentId(Long paymentId);
}
