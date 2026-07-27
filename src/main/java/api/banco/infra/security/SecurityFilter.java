package api.banco.infra.security;

import api.banco.model.Client;
import api.banco.repository.ClientRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    private ClientRepository repository;
    private TokenService tokenService;

    public SecurityFilter(ClientRepository clientRepository, TokenService tokenService){
        this.repository = clientRepository;
        this.tokenService = tokenService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String tokenJWT = recoveryToken(request);
        if (tokenJWT!=null){
            String subject = tokenService.getSubject(tokenJWT);
            Client user = repository.findByEmail(subject).orElseThrow();
            var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        }
        filterChain.doFilter(request, response);
    }

    private String recoveryToken (HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization ");
                if(authorizationHeader != null){
                    return authorizationHeader.replace("Bearer ", "");
                }
                return null;
    }
}
