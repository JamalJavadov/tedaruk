package az.att.admin.integration.asan.login;

import az.att.admin.integration.asan.certificates.dto.AsanLoginRequest;
import az.att.admin.integration.asan.login.dto.AsanLoginResponse;
import az.att.auth.dto.JwtPayloadDto;
import az.att.auth.dto.UserJwtDto;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@Profile("local")
public class AsanAuthClientMockImpl implements AsanAuthClient {

    @Override
    public AsanLoginResponse authenticate(AsanLoginRequest asanLoginRequest) {
        return AsanLoginResponse.builder()
                .accessToken("access-token")
                .idToken("id-token")
                .expiresIn(86400L)
                .tokenType("AUTH")
                .build();
    }

    @Override
    public JwtPayloadDto parseToken(AsanLoginResponse asanLoginResponse) {
        return JwtPayloadDto.builder()
                .sub("123456789")
                .user(UserJwtDto.builder()
                        .pin("AA1234567")
                        .name("John")
                        .surname("Doe")
                        .patronymic("Michael")
                        .citizenship("US")
                        .build())
                .iat(Instant.now().getEpochSecond())
                .exp(Instant.now().plus(1, ChronoUnit.DAYS).getEpochSecond())
                .type("ACCESS")
                .role("USER")
                .is_active(true)
                .build();

    }
}
