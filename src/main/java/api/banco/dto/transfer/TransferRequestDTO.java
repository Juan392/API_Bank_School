package api.banco.dto.transfer;

import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TransferRequestDTO(
        Long idAccountOrigin,
        String numberAccountDestiny,
        @Positive(message = "El monto a transferir debe ser mayor a cero")
        BigDecimal amount
) {
}
