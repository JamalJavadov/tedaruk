package az.att.admin.service.impl.auth;

import az.att.admin.entity.PortalUserEntity;
import az.att.admin.integration.asan.certificates.AsanCertificatesClient;
import az.att.admin.integration.asan.certificates.dto.AsanLoginRequest;
import az.att.admin.integration.asan.login.AsanAuthClient;
import az.att.admin.repository.UserDetailRepository;
import az.att.admin.repository.UserLoginRepository;
import az.att.admin.repository.OrganizationRepository;
import az.att.admin.service.OrganizationService;
import az.att.admin.service.UserRoleService;
import az.att.admin.service.impl.JwtTokenService;
import az.att.admin.service.impl.RefreshTokenService;
import az.att.admin.service.impl.UserCertificateServiceImpl;
import az.att.admin.service.impl.auth.dto.TokenResponseDto;
import az.att.admin.service.impl.users.UserMapper;
import az.att.admin.service.impl.users.UserServiceImpl;
import az.att.admin.service.impl.users.dto.PortalUser;
import az.att.exception.ApplicationException;
import az.att.exception.CommonErrors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@Profile("local")
@Primary
public class AuthServiceMockImpl extends AuthServiceImpl {

    public static final String MOCK_ADMIN_CODE = "ADMIN_MOCK_AUTH_CODE";
    public static final String MOCK_USER_CODE = "USER_MOCK_AUTH_CODE";
    public static final String MOCK_VIEWER_CODE = "VIEWER_MOCK_AUTH_CODE";

    public static final String ADMIN_PIN = "AZE1234567";
    public static final String USER_PIN = "AZE2345678";
    public static final String VIEWER_PIN = "AZE3456789";

    private final UserLoginRepository userLoginRepository;
    private final UserDetailRepository userDetailRepository;
    private final OrganizationRepository organizationRepository;
    private final UserMapper userMapper;
    private final JwtTokenService jwtTokenService;

    public AuthServiceMockImpl(AsanAuthClient asanAuthClient,
                              AsanCertificatesClient asanCertificatesClient,
                              JwtTokenService jwtTokenService,
                              RefreshTokenService refreshTokenService,
                              OrganizationService organizationService,
                              UserRoleService userRoleService,
                              UserServiceImpl userService,
                              UserCertificateServiceImpl userCertificateService,
                              UserLoginRepository userLoginRepository,
                              UserDetailRepository userDetailRepository,
                              OrganizationRepository organizationRepository,
                              UserMapper userMapper) {
        super(asanAuthClient, asanCertificatesClient, jwtTokenService, refreshTokenService,
                organizationService, userRoleService, userService, userCertificateService);
        this.userLoginRepository = userLoginRepository;
        this.userDetailRepository = userDetailRepository;
        this.organizationRepository = organizationRepository;
        this.userMapper = userMapper;
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    public TokenResponseDto signIn(AsanLoginRequest asanLoginRequest) {
        String authCode = asanLoginRequest.getAsanLoginInfo().getAuthCode();
        
        if (MOCK_ADMIN_CODE.equals(authCode)) {
            return mockSignIn(ADMIN_PIN);
        } else if (MOCK_USER_CODE.equals(authCode)) {
            return mockSignIn(USER_PIN);
        } else if (MOCK_VIEWER_CODE.equals(authCode)) {
            return mockSignIn(VIEWER_PIN);
        }

        return super.signIn(asanLoginRequest);
    }

    private TokenResponseDto mockSignIn(String pin) {
        log.info("Performing mock sign-in for PIN: {}", pin);
        PortalUserEntity userEntity = userLoginRepository.findByPinAndDeletedFalse(pin)
                .orElseThrow(() -> new ApplicationException(CommonErrors.ENTITY_NOT_FOUND));

        PortalUser portalUser = userMapper.toDto(userEntity);

        List<TokenResponseDto.Organization> organizationDtos = userDetailRepository
                .findAllByAsanUserId(userEntity.getId())
                .stream()
                .map(cert -> TokenResponseDto.Organization.builder()
                        .tin(cert.getTin())
                        .name(organizationRepository.findOrganizationByTin(cert.getTin())
                                .map(org -> org.getName())
                                .orElse(cert.getStructureName()))
                        .build())
                .toList();

        return buildTokenResponse(portalUser, organizationDtos);
    }

    private TokenResponseDto buildTokenResponse(PortalUser portalUser,
                                                List<TokenResponseDto.Organization> organizations) {
        return TokenResponseDto.builder()
                .accessToken(jwtTokenService.createAccessToken(portalUser))
                .refreshToken("")
                .organizations(organizations)
                .build();
    }
}
