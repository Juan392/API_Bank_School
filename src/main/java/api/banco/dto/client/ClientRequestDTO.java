package api.banco.dto.client;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record ClientRequestDTO(
        @NotNull @Email String email,
        @NotNull String password) {
}
