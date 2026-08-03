package api.banco.controller;

import api.banco.dto.account.AccountResponseDTO;
import api.banco.dto.account.CreateAccountRequestDTO;
import api.banco.dto.transfer.TransferHistoryDTO;
import api.banco.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cuentas")
public class AccountController {

    private final AccountService service;

    public AccountController(AccountService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<AccountResponseDTO>> getMyAccounts() {
        String emailAuth = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(service.getMyAccounts(emailAuth));
    }

    @PostMapping
    public ResponseEntity<AccountResponseDTO> openAccount(@RequestBody @Valid CreateAccountRequestDTO dto) {
        String emailAuth = SecurityContextHolder.getContext().getAuthentication().getName();
        AccountResponseDTO account = service.openAccount(dto, emailAuth);
        return ResponseEntity.status(HttpStatus.CREATED).body(account);
    }

    @GetMapping("/{id}/saldo")
    public ResponseEntity<Map<String, Object>> seeAmount(@PathVariable Long id) {
        String emailAuth = SecurityContextHolder.getContext().getAuthentication().getName();
        BigDecimal balance = service.seeAmount(id, emailAuth);
        return ResponseEntity.ok(Map.of("idAccount", id, "balance", balance));
    }

    @GetMapping("/{id}/movimientos")
    public ResponseEntity<List<TransferHistoryDTO>> moves(@PathVariable Long id) {
        String emailAuth = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(service.moves(id, emailAuth));
    }
}
