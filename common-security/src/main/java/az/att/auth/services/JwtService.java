package az.att.auth.services;

import az.att.auth.services.providers.ClaimProvider;
import az.att.auth.services.providers.ClaimSetProvider;
import az.att.config.SecurityProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public final class JwtService {

    private final Set<ClaimSetProvider> claimSetProviders;
    private final Set<ClaimProvider> claimProviders;
    private final SecurityProperties securityProperties;

    private Key hmacKey;
    private final ObjectMapper objectMapper = new ObjectMapper();

  
    @PostConstruct
    public void init() {
        byte[] keyBytes = Decoders.BASE64.decode(
                securityProperties.getJwtProperties().getSecret()
        );
        this.hmacKey = Keys.hmacShaKeyFor(keyBytes);
    }


 

    public String createTokenWithClaims(
            String subject,
            Map<String, Object> claims,
            Duration duration
    ) {
        Instant now = Instant.now();

        JwtBuilder builder = Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(duration)))
                .setHeader(Map.of("typ", "JWT"))
                .signWith(hmacKey, SignatureAlgorithm.HS512);

        if (claims != null && !claims.isEmpty()) {
            builder.addClaims(claims);
        }

        return builder.compact();
    }

    public Claims verifyJwt(String token, String jwksJson) throws Exception {
        String kid = extractKid(token);
        RSAPublicKey publicKey = getPublicKeyFromJwks(jwksJson, kid);

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        validateStandardClaims(claims);
        return claims;
    }

    private void validateStandardClaims(Claims claims) {
//        if (!"http://localhost:8081".equals(claims.getIssuer())) {
//            throw new SecurityException("Invalid issuer");
//        }

        if (claims.getAudience() == null ||
                !claims.getAudience().contains("08a43ded97fa4ad4a2fde117802cad2a")) {
            throw new SecurityException("Invalid audience");
        }
    }


    private String extractKid(String token) throws Exception {
        String headerJson = new String(
                Base64.getUrlDecoder().decode(token.split("\\.")[0])
        );
        JsonNode header = objectMapper.readTree(headerJson);

        if (!header.has("kid")) {
            throw new IllegalArgumentException("JWT header does not contain kid");
        }
        return header.get("kid").asText();
    }

    private RSAPublicKey getPublicKeyFromJwks(String jwksJson, String kid)
            throws Exception {

        JsonNode keys = objectMapper.readTree(jwksJson).get("keys");

        for (JsonNode key : keys) {
            if (kid.equals(key.get("kid").asText())) {

                BigInteger modulus = new BigInteger(1,
                        Base64.getUrlDecoder().decode(key.get("n").asText()));

                BigInteger exponent = new BigInteger(1,
                        Base64.getUrlDecoder().decode(key.get("e").asText()));

                RSAPublicKeySpec spec =
                        new RSAPublicKeySpec(modulus, exponent);

                return (RSAPublicKey) KeyFactory
                        .getInstance("RSA")
                        .generatePublic(spec);
            }
        }
        throw new IllegalArgumentException("Public key not found for kid: " + kid);
    }


    private void addClaimSets(JwtBuilder builder, Authentication auth) {
        claimSetProviders.forEach(provider -> {
            var claimSet = provider.provide(auth);
            builder.claim(claimSet.getKey(), claimSet.getClaims());
        });
    }

    private void addClaims(JwtBuilder builder, Authentication auth) {
        claimProviders.forEach(provider -> {
            var claim = provider.provide(auth);
            builder.claim(claim.getKey(), claim.getClaim());
        });
    }


    public <T> T extractAllClaims(String token, Class<T> clazz) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(hmacKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            String payloadJson = objectMapper.writeValueAsString(claims);
            return objectMapper.readValue(payloadJson, clazz);
        } catch (Exception e) {
            throw new RuntimeException("JWT signature validation or payload parsing error", e);
        }
    }

    /**
     * Claims-i istənilən tipə çevirir (Claims-dən DTO-ya)
     */
    public <T> T convertClaimsToDto(Claims claims, Class<T> clazz) {
        try {
            String payloadJson = objectMapper.writeValueAsString(claims);
            return objectMapper.readValue(payloadJson, clazz);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert Claims to DTO", e);
        }
    }
}
