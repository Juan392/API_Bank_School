package api.banco.service;

import api.banco.dto.transfer.TransferRequestDTO;
import api.banco.dto.transfer.TransferHistoryDTO;
import api.banco.model.Account;
import api.banco.model.Transfer;
import api.banco.repository.AccountRepository;
import api.banco.repository.TransferRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class TransferService {
    private AccountRepository accountRepository;
    private TransferRepository repository;

    public TransferService(AccountRepository accountRepository, TransferRepository transferRepository){
        this.accountRepository = accountRepository;
        this.repository = transferRepository;
    }

    @Transactional
    public TransferHistoryDTO transferring(TransferRequestDTO dto, String emailAuth){
        Account accountOrigen = accountRepository.findById(dto.idAccountOrigin()).orElseThrow(()-> new RuntimeException("Cuenta No Encontrada"));
        if(!accountOrigen.getIdClient().getEmail().equals(emailAuth)){
            throw new SecurityException("Operacion Denegada: Permisos insuficientes");
        }
        if(dto.amount().compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("El monto debe ser mayor a 0");
        }
        if(accountOrigen.getBalance().compareTo(dto.amount()) < 0){
            throw new IllegalArgumentException("Fondos Insuficientes para realizar la transferencia");
        }
        Account accountDestiny = accountRepository.findByAccountNumber(dto.numberAccountDestiny()).orElseThrow(()-> new RuntimeException("Cuenta No Encontrada"));
        accountOrigen.setBalance(accountOrigen.getBalance().subtract(dto.amount()));
        accountDestiny.setBalance(accountDestiny.getBalance().add(dto.amount()));

        Transfer newtransfer = new Transfer();
        newtransfer.setAccountOrigin(accountOrigen);
        newtransfer.setAccountDestiny(accountDestiny);
        newtransfer.setTransferDate(LocalDateTime.now());
        newtransfer.setAmount(dto.amount());

        repository.save(newtransfer);

        return new TransferHistoryDTO(
                newtransfer.getIdTransfer(), newtransfer.getAmount(), newtransfer.getTransferDate(),
                newtransfer.getAccountOrigin().getIdClient().getName(), newtransfer.getAccountDestiny().getIdClient().getName()
        );
    }
}
