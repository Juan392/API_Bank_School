package api.banco.dto;

public class Auth {
    public static record AuthResponseDTO(
            String token,
            String email
    ) {
    }
}
