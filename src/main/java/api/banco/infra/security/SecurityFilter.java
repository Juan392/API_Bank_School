package api.banco.infra.security;

import api.banco.repository.ClientRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private final ClientRepository clientRepository;
    private final TokenService tokenService;

    public SecurityFilter(ClientRepository clientRepository, TokenService tokenService) {
        this.clientRepository = clientRepository;
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String tokenJWT = recoveryToken(request);

        if (tokenJWT != null) {
            try {
                String subject = tokenService.getSubject(tokenJWT);
                if (subject != null) {
                    var userOptional = clientRepository.findByEmail(subject);
                    if (userOptional.isPresent()) {
                        var user = userOptional.get();
                        var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            } catch (Exception e) {
                // Token inválido o expirado: limpiamos contexto de seguridad y dejamos que Spring Security maneje la autorización
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }

    private String recoveryToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7).trim();
        }
        return null;
    }
}
