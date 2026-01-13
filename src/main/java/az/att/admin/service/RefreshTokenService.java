package az.att.admin.service;

import az.att.auth.services.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import az.att.auth.dto.JwtPayloadDto;
import az.att.admin.dto.RefreshTokenClaimsDto;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();

    private final JwtService jwtService;

    public String createRefreshToken(JwtPayloadDto parsedIdToken) {
        RefreshTokenClaimsDto.RefreshTokenClaimsDtoBuilder claimsBuilder = RefreshTokenClaimsDto.builder();
        claimsBuilder.sub(parsedIdToken != null ? parsedIdToken.getSub() : null);
        claimsBuilder.role(parsedIdToken != null ? parsedIdToken.getRole() : null);
        claimsBuilder.isActive(parsedIdToken != null ? parsedIdToken.getIs_active() : null);

        RefreshTokenClaimsDto claims = claimsBuilder.build();
        String subject = parsedIdToken != null && parsedIdToken.getSub() != null
                ? parsedIdToken.getSub()
                : UUID.randomUUID().toString();

        @SuppressWarnings("unchecked")
        java.util.Map<String, Object> claimsMap = (java.util.Map<String, Object>) objectMapper.convertValue(claims, java.util.Map.class);
        return jwtService.createTokenWithClaims(subject, claimsMap, Duration.ofSeconds(604800));
    }
}
