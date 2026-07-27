package api.banco.infra.security;

import api.banco.model.Client;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

@Service
public class TokenService {
    @Value("${banco.security.token.secret}")
    private String secret;
    public String tokenGenerator(Client client){
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.create().withIssuer("api_banco").withSubject(client.getEmail()).withExpiresAt(expirationDate()).sign(algorithm);
    }

    private Instant expirationDate(){
        return Instant.now().plus(2, ChronoUnit.HOURS);
    }

    public String getSubject(String tokenJWT){
        Algorithm algorithm = Algorithm.HMAC256(secret);
        try{
            return JWT.require(algorithm).withIssuer("api_banco")
                    .build()
                    .verify(tokenJWT)
                    .getSubject();
        }catch (JWTCreationException jwtEx){
            throw new RuntimeException("Token invalido");
        }
    }
}
