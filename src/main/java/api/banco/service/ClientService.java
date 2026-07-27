package api.banco.service;

import api.banco.dto.Auth;
import api.banco.dto.client.ClientRequestDTO;
import api.banco.infra.security.TokenService;
import api.banco.model.Client;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class ClientService {
    private TokenService service;
    private AuthenticationManager manager;

    public ClientService(TokenService token, AuthenticationManager manager){
        this.service = token;
        this.manager= manager;
    }

    public Auth.AuthResponseDTO login(ClientRequestDTO dto){
        var authenticationToken = new UsernamePasswordAuthenticationToken(dto.email(), dto.password());
        var authentication = manager.authenticate(authenticationToken);
        var token = service.tokenGenerator((Client) authentication.getPrincipal());
        return new Auth.AuthResponseDTO(token, dto.email());
    }
}
