package az.att.admin.integration.asan.login.jwt;

import az.att.exception.ApplicationException;
import az.att.exception.CommonErrors;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;

@Slf4j
@Component
public class AsanJwtService {

    private final ObjectMapper objectMapper = new ObjectMapper();

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

    private String extractKid(String token) throws Exception {
        String headerJson = new String(Base64.getUrlDecoder().decode(token.split("\\.")[0]));
        JsonNode header = objectMapper.readTree(headerJson);
        if (!header.has("kid")) {
            log.trace("JWT header does not contain kid");
            throw new ApplicationException(CommonErrors.HTTP_401);
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
        log.trace("Public key not found for kid:{}", kid);
        throw new ApplicationException(CommonErrors.HTTP_401);
    }

    private void validateStandardClaims(Claims claims) {
        //@ToDo: properly implement this section
        /*
        if (!"http://localhost:8081".equals(claims.getIssuer())) {
            throw new SecurityException("Invalid issuer");
        }

        if (claims.getAudience() == null ||
                !claims.getAudience().contains("08a43ded97fa4ad4a2fde117802cad2a")) {
            throw new SecurityException("Invalid audience");
        }*/
    }

    @SneakyThrows
    public <T> T convertClaimsToDto(Claims claims, Class<T> clazz) {
        String payloadJson = objectMapper.writeValueAsString(claims);
        return objectMapper.readValue(payloadJson, clazz);
    }

}
