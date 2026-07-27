package api.banco.dto.client;

import jakarta.validation.constraints.Email;

public record ClientResponseDTO(
        Long idClient,
        String name,
        String email
) {
}
