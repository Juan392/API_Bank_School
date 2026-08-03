package api.banco.controller;

import api.banco.dto.Auth;
import api.banco.dto.client.ClientRequestDTO;
import api.banco.dto.client.ClientResponseDTO;
import api.banco.dto.client.RegisterRequestDTO;
import api.banco.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class ClientController {

    private final ClientService service;

    public ClientController(ClientService service) {
        this.service = service;
    }

    @PostMapping("/login")
    public ResponseEntity<Auth.AuthResponseDTO> login(@RequestBody @Valid ClientRequestDTO dto) {
        return ResponseEntity.ok(service.login(dto));
    }

    @PostMapping("/register")
    public ResponseEntity<ClientResponseDTO> register(@RequestBody @Valid RegisterRequestDTO dto) {
        ClientResponseDTO response = service.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/me")
    public ResponseEntity<ClientResponseDTO> getMyProfile() {
        String emailAuth = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(service.getProfile(emailAuth));
    }
}
