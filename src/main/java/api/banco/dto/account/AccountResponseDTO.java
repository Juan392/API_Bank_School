package api.banco.dto.account;

import java.math.BigDecimal;

public record AccountResponseDTO(
        Long idAccount,
        String accountNumber,
        BigDecimal balance,
        boolean isActive,
        String clientName,
        String clientEmail
) {
}
