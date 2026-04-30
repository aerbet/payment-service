package kg.zhaparov.paymentsystem.api;

import kg.zhaparov.paymentsystem.api.dto.CreatePaymentRequest;
import kg.zhaparov.paymentsystem.api.dto.PaymentDto;
import kg.zhaparov.paymentsystem.domain.PaymentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public PaymentDto create(@Valid @RequestBody CreatePaymentRequest request) {
        return paymentService.createPayment(request);
    }

    @GetMapping("/{id}")
    public PaymentDto get(@PathVariable Long id) {
        return paymentService.getPayment(id);
    }

    @PostMapping("/{id}/confirm")
    public PaymentDto confirm(@PathVariable Long id) {
        return paymentService.confirmPayment(id);
    }
}
