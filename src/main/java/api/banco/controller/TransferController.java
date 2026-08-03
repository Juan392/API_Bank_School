package api.banco.controller;

import api.banco.dto.transfer.TransferHistoryDTO;
import api.banco.dto.transfer.TransferRequestDTO;
import api.banco.service.TransferService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transacciones")
public class TransferController {

    private final TransferService service;

    public TransferController(TransferService service) {
        this.service = service;
    }

    @PostMapping("/transferir")
    public ResponseEntity<TransferHistoryDTO> transferring(@RequestBody @Valid TransferRequestDTO dto) {
        String emailAuth = SecurityContextHolder.getContext().getAuthentication().getName();
        TransferHistoryDTO result = service.transferring(dto, emailAuth);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
}
