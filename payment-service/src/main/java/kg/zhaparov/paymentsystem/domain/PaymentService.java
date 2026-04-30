package kg.zhaparov.paymentsystem.domain;

import kg.zhaparov.paymentsystem.api.dto.CreatePaymentRequest;
import kg.zhaparov.paymentsystem.api.dto.PaymentDto;
import kg.zhaparov.paymentsystem.domain.db.PaymentEntity;
import kg.zhaparov.paymentsystem.domain.db.PaymentRepository;
import kg.zhaparov.paymentsystem.mapper.PaymentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);
    private static final BigDecimal MAX_AMOUNT = new BigDecimal("10000.00");

    private final PaymentMapper mapper;
    private final PaymentRepository paymentRepository;

    public PaymentService(
            PaymentMapper mapper,
            PaymentRepository paymentRepository
    ) {
        this.paymentRepository = paymentRepository;
        this.mapper = mapper;
    }

    @Transactional
    public PaymentDto createPayment(CreatePaymentRequest request) {
        PaymentEntity payment = new PaymentEntity(request.userId(), request.amount(), PaymentStatus.NEW);
        PaymentEntity saved = paymentRepository.save(payment);
        log.info("Payment created: id={}", saved.getId());

        return mapper.entityToDto(saved);
    }

    public PaymentDto getPayment(Long id) {
        PaymentEntity payment = findPaymentOrThrow(id);
        return mapper.entityToDto(payment);
    }

    public PaymentDto confirmPayment(Long id) {
        PaymentEntity payment = findPaymentOrThrow(id);
        payment.setStatus(PaymentStatus.SUCCEEDED);
        PaymentEntity saved = paymentRepository.save(payment);
        log.info("Payment has been confirmed: id={}", id);

        return mapper.entityToDto(saved);
    }

    private PaymentEntity findPaymentOrThrow(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment with id=" + id + " not found"));
    }
}
