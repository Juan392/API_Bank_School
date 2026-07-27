package api.banco.service;


import api.banco.dto.transfer.TransferHistoryDTO;
import api.banco.model.Account;
import api.banco.repository.AccountRepository;
import api.banco.repository.TransferRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountService {
    private AccountRepository repository;
    private TransferRepository transferRepository;

    public AccountService(AccountRepository repository, TransferRepository transferRepository) {
        this.repository = repository;
        this.transferRepository = transferRepository;
    }

    public BigDecimal seeAmount(Long id, String emailAuth){
        Account account = repository.findById(id).orElseThrow(()-> new RuntimeException("Cuenta No Encontrada"));
        if(!account.getIdClient().getUsername().equals(emailAuth)){
            throw new SecurityException("No tienes permisos para ver los movimientos de esta cuenta");
        }
        return account.getBalance();
    }

    public List<TransferHistoryDTO> moves(Long id, String emailAuth){
        Account account = repository.findById(id).orElseThrow(()-> new RuntimeException("Cuenta No Encontrada"));
        if(!account.getIdClient().getUsername().equals(emailAuth)){
            throw new SecurityException("No tienes permisos para ver los movimientos de esta cuenta");
        }
        return transferRepository.searchMovesWithJoin(id);
    }
}
