package api.banco.service;

import api.banco.dto.Auth;
import api.banco.dto.client.ClientRequestDTO;
import api.banco.dto.client.ClientResponseDTO;
import api.banco.dto.client.RegisterRequestDTO;
import api.banco.infra.security.TokenService;
import api.banco.model.Client;
import api.banco.repository.ClientRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClientService {

    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;

    public ClientService(TokenService tokenService,
                         AuthenticationManager authenticationManager,
                         ClientRepository clientRepository,
                         PasswordEncoder passwordEncoder) {
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Auth.AuthResponseDTO login(ClientRequestDTO dto) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(dto.email(), dto.password());
        var authentication = authenticationManager.authenticate(authenticationToken);
        var client = (Client) authentication.getPrincipal();
        var token = tokenService.tokenGenerator(client);
        return new Auth.AuthResponseDTO(token, client.getEmail());
    }

    @Transactional
    public ClientResponseDTO register(RegisterRequestDTO dto) {
        if (clientRepository.existsByEmail(dto.email())) {
            throw new IllegalArgumentException("Ya existe un cliente registrado con el email: " + dto.email());
        }

        String encodedPassword = passwordEncoder.encode(dto.password());
        Client client = new Client(dto.name(), dto.email(), encodedPassword);
        Client savedClient = clientRepository.save(client);

        return new ClientResponseDTO(savedClient.getIdClient(), savedClient.getName(), savedClient.getEmail());
    }

    public ClientResponseDTO getProfile(String emailAuth) {
        Client client = clientRepository.findByEmail(emailAuth)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));
        return new ClientResponseDTO(client.getIdClient(), client.getName(), client.getEmail());
    }
}
