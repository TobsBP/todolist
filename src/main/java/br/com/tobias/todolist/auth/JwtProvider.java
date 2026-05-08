package br.com.tobias.todolist.auth;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

@Component
public class JwtProvider {

    private final Algorithm algorithm;
    private final JWTVerifier verifier;
    private final long expirationHours;
    private static final String ISSUER = "todolist";

    public JwtProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration-hours}") long expirationHours) {
        this.algorithm = Algorithm.HMAC256(secret);
        this.verifier = JWT.require(algorithm).withIssuer(ISSUER).build();
        this.expirationHours = expirationHours;
    }

    public String generate(UUID userID) {
        var now = Instant.now();
        return JWT.create()
                .withIssuer(ISSUER)
                .withSubject(userID.toString())
                .withIssuedAt(now)
                .withExpiresAt(now.plus(expirationHours, ChronoUnit.HOURS))
                .sign(algorithm);
    }

    public UUID verify(String token) {
        try {
            DecodedJWT decoded = verifier.verify(token);
            return UUID.fromString(decoded.getSubject());
        } catch (JWTVerificationException | IllegalArgumentException e) {
            return null;
        }
    }

    public long getExpirationHours() {
        return expirationHours;
    }
}
