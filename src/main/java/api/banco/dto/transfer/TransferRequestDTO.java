package api.banco.dto.transfer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TransferRequestDTO(
        @NotNull(message = "El ID de la cuenta origen es obligatorio")
        Long idAccountOrigin,

        @NotBlank(message = "El número de cuenta destino es obligatorio")
        String numberAccountDestiny,

        @NotNull(message = "El monto es obligatorio")
        @Positive(message = "El monto a transferir debe ser mayor a cero")
        BigDecimal amount
) {
}
