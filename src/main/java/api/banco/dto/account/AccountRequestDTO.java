package api.banco.dto.account;

import java.math.BigDecimal;

public record AccountRequestDTO(
        String numberAccount,
        BigDecimal balance,
        boolean isActive
) {
}
