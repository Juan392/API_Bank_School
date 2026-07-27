package api.banco.controller;

import api.banco.dto.client.ClientRequestDTO;
import api.banco.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class ClientController {
    private ClientService service;
    public ClientController(ClientService service){
        this.service = service;
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid ClientRequestDTO dto){
        return ResponseEntity.ok(service.login(dto));
    }
}
