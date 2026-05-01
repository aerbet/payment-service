package kg.zhaparov.paymentsystem.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateUserRequest(
        @Schema(description = "mail пользователя", example = "pasha@mail.ru")
        @NotBlank
        @Email String email
) {
}
