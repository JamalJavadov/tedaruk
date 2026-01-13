package az.att.admin.service;

import az.att.admin.integration.asan.certificates.AsanCertificatesClient;
import az.att.admin.integration.asan.certificates.dto.AsanCertificatesResponseDto;
import az.att.admin.integration.asan.certificates.dto.AsanLoginRequest;
import az.att.admin.integration.asan.certificates.dto.CertificateData;
import az.att.admin.dto.TokenResponseDto;
import az.att.admin.entity.AsanUserCertificatesEntity;
import az.att.admin.entity.AsanUserEntity;
import az.att.admin.integration.asan.certificates.dto.CertificateDto;
import az.att.admin.integration.asan.login.AsanAuthClient;
import az.att.admin.integration.asan.login.dto.AsanLoginResponse;
import az.att.admin.integration.asan.login.dto.StructureDataDto;
import az.att.admin.mapper.CertificateMapper;
import az.att.admin.repository.UserDetailRepository;
import az.att.admin.repository.UserLoginRepository;
import az.att.auth.dto.JwtPayloadDto;
import az.att.exception.ApplicationException;
import az.att.exception.CommonErrors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AsanAuthClient asanAuthClient;
    private final AsanCertificatesClient asanCertificatesClient;
    private final AttTokenService attTokenService;
    private final RefreshTokenService refreshTokenService;
    private final UserLoginRepository userLoginRepository;
    private final UserDetailRepository userDetailRepository;
    private final CertificateMapper certificateMapper;

    @Transactional
    public TokenResponseDto signIn(AsanLoginRequest asanLoginRequest) {
        AsanLoginResponse asanLoginResponse = asanAuthClient.authenticate(asanLoginRequest);
        AsanCertificatesResponseDto certificates = asanCertificatesClient.getCertificates(asanLoginResponse.getAccessToken());
        CertificateData certificateData = getCertificateData(certificates);
        JwtPayloadDto parsedIdToken = asanAuthClient.parseToken(asanLoginResponse);
        AsanUserEntity user = createOrUpdateUser(parsedIdToken);
        saveOrUpdateCertificate(user, certificateData);
        return TokenResponseDto.builder()
                .accessToken(attTokenService.createAttToken(parsedIdToken, certificateData))
                .refreshToken(refreshTokenService.createRefreshToken(parsedIdToken))
                .build();
    }

    private AsanUserEntity createOrUpdateUser(JwtPayloadDto payload) {
        if (payload.getUser() == null || payload.getUser().getPin() == null) {
            throw new az.att.exception.ApplicationException(az.att.exception.CommonErrors.ENTITY_NOT_FOUND);
        }

        String pin = payload.getUser().getPin();
        AsanUserEntity user = userLoginRepository
                .findByPin(pin)
                .orElseGet(() -> AsanUserEntity.builder()
                        .pin(pin)
                        .name(payload.getUser().getName())
                        .surname(payload.getUser().getSurname())
                        .patronymic(payload.getUser().getPatronymic())
                        .citizenship(payload.getUser().getCitizenship())
                        .role(payload.getRole())
                        .isActive(true) //@ToDo: verify what if user is disabled in our system
                        .build());

        user.setLastActiveTime(LocalDateTime.now(ZoneId.of("Asia/Baku")));
        return userLoginRepository.save(user);
    }

    private void saveOrUpdateCertificate(AsanUserEntity user, CertificateData certificateData) {
        AsanUserCertificatesEntity certificate =
                userDetailRepository.findByAsanUserAndVoen(user, certificateData.getVoen())
                        .orElseGet(() -> {
                            AsanUserCertificatesEntity entity = new AsanUserCertificatesEntity();
                            entity.setAsanUser(user);
                            entity.setVoen(certificateData.getVoen());
                            return entity;
                        });

        // Map incoming data onto entity (both for update and create)
        certificateMapper.updateEntityFromDto(certificateData, certificate);

        userDetailRepository.save(certificate);
    }

    public CertificateData getCertificateData(AsanCertificatesResponseDto response) {
        String phoneNumber = getPhoneNumber(response);
        List<CertificateDto> certificates = response.getCertificates();

        if (certificates == null || certificates.isEmpty()) {
            return CertificateData.builder()
                    .phoneNumber(phoneNumber)
                    .build();
        }

        StructureDataDto structureData = response.getCertificates().get(0).getStructureData();

        return CertificateData.builder()
                .phoneNumber(phoneNumber)
                .voen(structureData.getVoen())
                .structureName(structureData.getStructureName())
                .position(structureData.getPosition())
                .hasStamp(structureData.getHasStamp())
                .legal(structureData.getLegal())
                .build();
    }

    private String getPhoneNumber(AsanCertificatesResponseDto response) {
        if (response.getLoginDetail() == null || response.getLoginDetail().getPhoneNumber() == null) {
            log.warn("Phone number is null");
            throw new ApplicationException(CommonErrors.HTTP_401);
        }
        return response.getLoginDetail().getPhoneNumber();
    }
}
