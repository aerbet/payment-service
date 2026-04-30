package kg.zhaparov.paymentsystem.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CreatePaymentRequest(
        @Schema(description = "id плательщика", example = "1")
        @NotNull
        @Positive
        Long userId,
        @Schema(description = "сумма платежа", example = "1000")
        @NotNull
        @Positive
        BigDecimal amount
) {
}
