package api.banco.controller;

import api.banco.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cuentas")
public class AccountController {
    private AccountService service;
    public AccountController(AccountService service){
        this.service = service;
    }

    @GetMapping("/{id}/saldo")
    public ResponseEntity<?> seeAmount(@PathVariable Long id){
        String emailAuth = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok().body(service.seeAmount(id, emailAuth));
    }

    @GetMapping("/{id}/movimientos")
    public ResponseEntity<?> moves(@PathVariable Long id){
        String emailAuth = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok().body(service.moves(id, emailAuth));
    }
}
