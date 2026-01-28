package az.att.admin.service.impl.auth;

import az.att.admin.integration.asan.login.dto.AsanJwtResponse;
import az.att.admin.service.impl.JwtTokenService;
import az.att.admin.service.impl.RefreshTokenService;
import az.att.admin.service.impl.UserCertificateServiceImpl;
import az.att.admin.integration.asan.certificates.AsanCertificatesClient;
import az.att.admin.integration.asan.certificates.dto.AsanCertificatesResponseDto;
import az.att.admin.integration.asan.certificates.dto.AsanLoginRequest;
import az.att.admin.integration.asan.certificates.dto.CertificateDto;
import az.att.admin.integration.asan.login.AsanAuthClient;
import az.att.admin.integration.asan.login.dto.AsanLoginResponse;
import az.att.admin.service.AuthService;
import az.att.admin.service.OrganizationService;
import az.att.admin.service.UserRoleService;
import az.att.admin.service.impl.auth.dto.OrganizationSelectionRequestDto;
import az.att.admin.service.impl.auth.dto.OrganizationTokenResponseDto;
import az.att.admin.service.impl.auth.dto.SimpleTokenResponseDto;
import az.att.admin.service.impl.auth.dto.TokenResponseDto;
import az.att.admin.service.impl.auth.dto.UserRoleRequestDto;
import az.att.admin.service.impl.organization.dto.Organization;
import az.att.admin.service.impl.roles.dto.PortalUserRole;
import az.att.admin.service.impl.users.UserServiceImpl;
import az.att.admin.service.impl.users.dto.PortalUser;
import az.att.exception.ApplicationException;
import az.att.exception.CommonErrors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import az.att.admin.config.auth.UserPrincipal;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AsanAuthClient asanAuthClient;
    private final AsanCertificatesClient asanCertificatesClient;
    private final JwtTokenService jwtTokenService;
    private final RefreshTokenService refreshTokenService;
    private final OrganizationService organizationService;
    private final UserRoleService userRoleService;

    private final UserServiceImpl userService;
    private final UserCertificateServiceImpl userCertificateService;

    @Override
    public TokenResponseDto signIn(AsanLoginRequest asanLoginRequest) {
        AsanLoginResponse loginResponse = asanAuthClient.authenticate(asanLoginRequest);
        AsanCertificatesResponseDto certificatesResponse = asanCertificatesClient
                .getCertificates(loginResponse.getAccessToken());
        AsanJwtResponse parsedIdToken = asanAuthClient.parseToken(loginResponse);

        String phone = loginResponse.getLoginDetail() != null ?
                loginResponse.getLoginDetail().getPhoneNumber() : null;
        if (phone == null && certificatesResponse.getLoginDetail() != null) {
            phone = certificatesResponse.getLoginDetail().getPhoneNumber();
        }

        PortalUser user = userService.createOrUpdateUser(parsedIdToken, phone);
        List<CertificateDto> validCertificates = userCertificateService.filterValidCertificates(certificatesResponse);
        userCertificateService.saveUserCertificates(user, validCertificates);
        List<TokenResponseDto.Organization> organizationDtos = mapToCertificateDtos(validCertificates);
        return buildTokenResponse(user, organizationDtos);
    }

    @Override
    public OrganizationTokenResponseDto setOrganization(OrganizationSelectionRequestDto request,
                                                        UserPrincipal principal) {
        List<Organization> organizations = organizationService.findByUser(principal.getUserId());
        Organization selectedOrg = organizations.stream()
                .filter(organization -> request.getTin().equalsIgnoreCase(organization.getTin()))
                .findFirst()
                .orElseThrow(() -> new ApplicationException(CommonErrors.HTTP_401));
        principal.setHasStamp(selectedOrg.getHasStamp());

        List<PortalUserRole> userUserRoleList = userRoleService.findUserRoles(principal.getUserId(), request.getTin());
        return OrganizationTokenResponseDto.builder()
                .accessToken(jwtTokenService.createAccessToken(principal, request.getTin()))
                .refreshToken("")
                .userRoles(userUserRoleList)
                .build();
    }

    @Override
    public SimpleTokenResponseDto setUserRole(UserRoleRequestDto requestBody, UserPrincipal principal) {
        if (userRoleService.findUserRoles(principal.getUserId(), principal.getTin())
                .stream().filter(userRole -> userRole.getId().equals(requestBody.getRoleId()))
                .findAny().isEmpty()) {
            throw new ApplicationException(CommonErrors.HTTP_401);
        }
        List<String> permissions = userRoleService.findPermissions(requestBody.getRoleId());
        String accessToken = jwtTokenService.createAccessToken(principal, permissions);
        return SimpleTokenResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken("")
                .build();
    }

    private List<TokenResponseDto.Organization> mapToCertificateDtos(List<CertificateDto> certificates) {
        return certificates.stream()
                .map(cert -> TokenResponseDto.Organization.builder()
                        .tin(cert.getStructureData().getVoen())
                        .name(cert.getStructureData().getStructureName())
                        .build())
                .toList();
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