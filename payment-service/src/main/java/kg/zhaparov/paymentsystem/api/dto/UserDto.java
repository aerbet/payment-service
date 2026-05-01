package kg.zhaparov.paymentsystem.api.dto;

import java.util.List;

public record UserDto(
        Long id,
        String email,
        List<PaymentDto> payments
) {
}
