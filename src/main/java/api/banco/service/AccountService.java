package api.banco.service;

import api.banco.dto.account.AccountResponseDTO;
import api.banco.dto.account.CreateAccountRequestDTO;
import api.banco.dto.transfer.TransferHistoryDTO;
import api.banco.model.Account;
import api.banco.model.Client;
import api.banco.repository.AccountRepository;
import api.banco.repository.ClientRepository;
import api.banco.repository.TransferRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

@Service
public class AccountService {

    private final AccountRepository repository;
    private final TransferRepository transferRepository;
    private final ClientRepository clientRepository;

    public AccountService(AccountRepository repository,
                          TransferRepository transferRepository,
                          ClientRepository clientRepository) {
        this.repository = repository;
        this.transferRepository = transferRepository;
        this.clientRepository = clientRepository;
    }

    public BigDecimal seeAmount(Long id, String emailAuth) {
        Account account = getAccountAndVerifyOwnership(id, emailAuth);
        return account.getBalance();
    }

    public List<TransferHistoryDTO> moves(Long id, String emailAuth) {
        getAccountAndVerifyOwnership(id, emailAuth);
        return transferRepository.searchMovesWithJoin(id);
    }

    public List<AccountResponseDTO> getMyAccounts(String emailAuth) {
        return repository.findByClient_Email(emailAuth).stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Transactional
    public AccountResponseDTO openAccount(CreateAccountRequestDTO dto, String emailAuth) {
        Client client = clientRepository.findByEmail(emailAuth)
                .orElseThrow(() -> new NoSuchElementException("Cliente no encontrado"));

        String accountNumber = generateUniqueAccountNumber();
        Account account = new Account(client, accountNumber, dto.initialDeposit(), true);
        Account savedAccount = repository.save(account);

        return mapToDTO(savedAccount);
    }

    private Account getAccountAndVerifyOwnership(Long accountId, String emailAuth) {
        Account account = repository.findById(accountId)
                .orElseThrow(() -> new NoSuchElementException("Cuenta no encontrada con ID: " + accountId));

        if (!account.getClient().getEmail().equals(emailAuth)) {
            throw new SecurityException("Operación Denegada: No tienes permisos sobre esta cuenta.");
        }

        if (!account.isActive()) {
            throw new IllegalArgumentException("La cuenta se encuentra inactiva.");
        }

        return account;
    }

    private String generateUniqueAccountNumber() {
        Random random = new Random();
        String number;
        do {
            int randomDigits = 1000 + random.nextInt(9000);
            number = "ACC-" + randomDigits;
        } while (repository.existsByAccountNumber(number));
        return number;
    }

    private AccountResponseDTO mapToDTO(Account account) {
        return new AccountResponseDTO(
                account.getIdAccount(),
                account.getAccountNumber(),
                account.getBalance(),
                account.isActive(),
                account.getClient().getName(),
                account.getClient().getEmail()
        );
    }
}
