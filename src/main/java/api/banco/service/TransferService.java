package api.banco.service;

import api.banco.dto.transfer.TransferHistoryDTO;
import api.banco.dto.transfer.TransferRequestDTO;
import api.banco.model.Account;
import api.banco.model.Transfer;
import api.banco.repository.AccountRepository;
import api.banco.repository.TransferRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Service
public class TransferService {

    private final AccountRepository accountRepository;
    private final TransferRepository transferRepository;

    public TransferService(AccountRepository accountRepository, TransferRepository transferRepository) {
        this.accountRepository = accountRepository;
        this.transferRepository = transferRepository;
    }

    @Transactional
    public TransferHistoryDTO transferring(TransferRequestDTO dto, String emailAuth) {
        if (dto.amount() == null || dto.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto a transferir debe ser mayor a 0");
        }

        Account accountOrigen = accountRepository.findById(dto.idAccountOrigin())
                .orElseThrow(() -> new NoSuchElementException("Cuenta origen no encontrada con ID: " + dto.idAccountOrigin()));

        if (!accountOrigen.getClient().getEmail().equals(emailAuth)) {
            throw new SecurityException("Operación Denegada: No tienes permisos para transferir desde esta cuenta");
        }

        if (!accountOrigen.isActive()) {
            throw new IllegalArgumentException("La cuenta origen se encuentra inactiva");
        }

        Account accountDestiny = accountRepository.findByAccountNumber(dto.numberAccountDestiny())
                .orElseThrow(() -> new NoSuchElementException("Cuenta destino no encontrada con número: " + dto.numberAccountDestiny()));

        if (!accountDestiny.isActive()) {
            throw new IllegalArgumentException("La cuenta destino se encuentra inactiva");
        }

        if (accountOrigen.getIdAccount().equals(accountDestiny.getIdAccount())) {
            throw new IllegalArgumentException("No se puede realizar una transferencia a la misma cuenta de origen");
        }

        if (accountOrigen.getBalance().compareTo(dto.amount()) < 0) {
            throw new IllegalArgumentException("Fondos insuficientes para realizar la transferencia. Saldo actual: $" + accountOrigen.getBalance());
        }

        // Ejecutar débitos y créditos
        accountOrigen.setBalance(accountOrigen.getBalance().subtract(dto.amount()));
        accountDestiny.setBalance(accountDestiny.getBalance().add(dto.amount()));

        accountRepository.save(accountOrigen);
        accountRepository.save(accountDestiny);

        Transfer newTransfer = new Transfer(accountOrigen, accountDestiny, dto.amount(), LocalDateTime.now());
        Transfer savedTransfer = transferRepository.save(newTransfer);

        return new TransferHistoryDTO(
                savedTransfer.getIdTransfer(),
                savedTransfer.getAmount(),
                savedTransfer.getTransferDate(),
                savedTransfer.getAccountOrigin().getClient().getName(),
                savedTransfer.getAccountDestiny().getClient().getName()
        );
    }
}
