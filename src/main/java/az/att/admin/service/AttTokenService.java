package az.att.admin.service;

import az.att.auth.dto.JwtPayloadDto;
import az.att.auth.services.JwtService;

import az.att.admin.integration.asan.certificates.dto.CertificateData;
import az.att.admin.integration.asan.certificates.dto.AttTokenClaimsDto;
import az.att.admin.integration.asan.login.dto.StructureDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AttTokenService {
    private final com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();

    private final JwtService jwtService;

    @Value("${att.token.duration:3600}")
    private int tokenDurationSeconds;

    public String createAttToken(JwtPayloadDto parsedIdToken, CertificateData certificateData) {
        AttTokenClaimsDto.AttTokenClaimsDtoBuilder claimsBuilder = AttTokenClaimsDto.builder();

        if (parsedIdToken != null) {
            claimsBuilder.sub(parsedIdToken.getSub());
            claimsBuilder.user(parsedIdToken.getUser());
            claimsBuilder.isActive(parsedIdToken.getIs_active());
            claimsBuilder.role(parsedIdToken.getRole());
        }

        if (certificateData != null) {
            claimsBuilder.voen(certificateData.getVoen());
            claimsBuilder.phoneNumber(certificateData.getPhoneNumber());
            if (certificateData.getVoen() != null || certificateData.getStructureName() != null ||
                    certificateData.getPosition() != null || certificateData.getHasStamp() != null ||
                    certificateData.getLegal() != null) {
                claimsBuilder.structure(StructureDto.builder()
                        .voen(certificateData.getVoen())
                        .structureName(certificateData.getStructureName())
                        .position(certificateData.getPosition())
                        .hasStamp(certificateData.getHasStamp())
                        .legal(certificateData.getLegal())
                        .build());
            }
        }

        AttTokenClaimsDto claims = claimsBuilder.build();
        String subject = parsedIdToken != null && parsedIdToken.getSub() != null
            ? parsedIdToken.getSub()
            : UUID.randomUUID().toString();

        @SuppressWarnings("unchecked")
        java.util.Map<String, Object> claimsMap = (java.util.Map<String, Object>) objectMapper.convertValue(claims, java.util.Map.class);
        return jwtService.createTokenWithClaims(subject, claimsMap, Duration.ofSeconds(tokenDurationSeconds));
    }
}
