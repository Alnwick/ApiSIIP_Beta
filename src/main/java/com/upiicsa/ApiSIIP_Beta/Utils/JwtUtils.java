package com.upiicsa.ApiSIIP_Beta.Utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.upiicsa.ApiSIIP_Beta.Model.UserSIIP;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.stream.Collectors;

@Component
public class JwtUtils {

    @Value("")
    private String privateKey;

    @Value("")
    private String userGenerator;

    public String createToken(Authentication authentication) {
        try {
            UserSIIP userPrincipal = (UserSIIP) authentication.getPrincipal();
            Algorithm algorithm = Algorithm.HMAC256(privateKey);

            // Obtain permissions from the authenticated user
            String authorities = authentication.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(","));

            // Create token for the user
            return JWT.create()
                    .withIssuer(userGenerator)
                    .withSubject(userPrincipal.getEmail())
                    .withClaim("id", userPrincipal.getId())
                    .withClaim("authorities", authorities)
                    .withExpiresAt(generarFechaExpiracion())
                    .sign(algorithm);
        } catch (JWTVerificationException exception) {
            throw new JWTVerificationException("Error to create JWT Token", exception);
        }
    }

    public DecodedJWT validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(privateKey);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(userGenerator)
                    .build();
            return verifier.verify(token);
        } catch (JWTVerificationException exception) {
            throw new JWTVerificationException("Token invalid o r expired. Not authorized.");
        }
    }

    public String extractUsername(DecodedJWT decodedJWT) {
        return decodedJWT.getSubject();
    }

    public Claim getSpecificClaim(DecodedJWT decodedJWT, String claimName) {
        return decodedJWT.getClaim(claimName);
    }

    private Instant generarFechaExpiracion() {
        // Token expired in 3 hours
        return LocalDateTime.now().plusHours(3).toInstant(ZoneOffset.of("-05:00"));
    }
}
