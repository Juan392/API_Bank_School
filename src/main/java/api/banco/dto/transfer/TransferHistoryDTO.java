package api.banco.dto.transfer;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransferHistoryDTO(
        Long idTransaccion,
        BigDecimal amount,
        LocalDateTime transferDate,
        String nameOrigin,
        String nameDestiny) {
}
