package api.banco.repository;

import api.banco.model.Account;
import api.banco.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByAccountNumber(String accountNumber);
    List<Account> findByClient(Client client);
    List<Account> findByClient_Email(String email);
    boolean existsByAccountNumber(String accountNumber);
}
