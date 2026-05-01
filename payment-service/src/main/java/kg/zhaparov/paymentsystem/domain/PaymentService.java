package kg.zhaparov.paymentsystem.domain;

import kg.zhaparov.paymentsystem.api.dto.CreatePaymentRequest;
import kg.zhaparov.paymentsystem.api.dto.PaymentDto;
import kg.zhaparov.paymentsystem.domain.db.PaymentEntity;
import kg.zhaparov.paymentsystem.domain.db.PaymentRepository;
import kg.zhaparov.paymentsystem.domain.db.UserRepository;
import kg.zhaparov.paymentsystem.mapper.PaymentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

@Service
public class PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);
    private static final BigDecimal MAX_AMOUNT = new BigDecimal("10000.00");

    private final PaymentMapper mapper;
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final RedisTemplate<String, PaymentDto> redisTemplate;

    public PaymentService(
            PaymentMapper mapper,
            PaymentRepository paymentRepository, UserRepository userRepository, RedisTemplate<String, PaymentDto> redisTemplate
    ) {
        this.paymentRepository = paymentRepository;
        this.mapper = mapper;
        this.userRepository = userRepository;
        this.redisTemplate = redisTemplate;
    }

    @Transactional
    public PaymentDto createPayment(CreatePaymentRequest request) {
        if (!userRepository.existsById(request.userId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id = " + request.userId());
        }
        if (request.amount().compareTo(MAX_AMOUNT) > 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You've reached the maximum amount.");
        }
        PaymentEntity payment = new PaymentEntity(request.userId(), request.amount(), PaymentStatus.NEW);
        PaymentEntity saved = paymentRepository.save(payment);
        log.info("Payment created: id={}", saved.getId());

        return mapper.entityToDto(saved);
    }

    public PaymentDto getPayment(Long id) {
        var foundInCache = redisTemplate.opsForValue().get(id.toString());
        if (foundInCache != null) {
            log.info("Payment found in cache: id={}", id);
            return foundInCache;
        }
        PaymentEntity payment = findPaymentOrThrow(id);
        PaymentDto paymentDto = mapper.entityToDto(payment);
        redisTemplate.opsForValue().set(id.toString(), paymentDto);
        return mapper.entityToDto(payment);
    }

    public PaymentDto confirmPayment(Long id) {
        PaymentEntity payment = findPaymentOrThrow(id);
        if (payment.getStatus() != PaymentStatus.NEW) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Payment status must be NEW");
        }
        payment.setStatus(PaymentStatus.SUCCEEDED);
        PaymentEntity saved = paymentRepository.save(payment);
        log.info("Payment has been confirmed: id={}", id);
        redisTemplate.delete(id.toString());
        return mapper.entityToDto(saved);
    }

    private PaymentEntity findPaymentOrThrow(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Payment with id=" + id + " not found")
                );
    }
}
