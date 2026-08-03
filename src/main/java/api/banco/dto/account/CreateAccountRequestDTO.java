package api.banco.dto.account;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateAccountRequestDTO(
        @NotNull(message = "El depósito inicial es obligatorio")
        @DecimalMin(value = "0.00", message = "El saldo inicial no puede ser negativo")
        BigDecimal initialDeposit
) {
}
