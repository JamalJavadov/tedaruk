package az.att.admin.service;

import az.att.admin.config.auth.UserPrincipal;
import az.att.admin.service.impl.auth.dto.OrganizationSelectionRequestDto;
import az.att.admin.service.impl.auth.dto.OrganizationTokenResponseDto;
import az.att.admin.service.impl.auth.dto.SimpleTokenResponseDto;
import az.att.admin.service.impl.auth.dto.TokenResponseDto;
import az.att.admin.service.impl.auth.dto.UserRoleRequestDto;
import az.att.admin.integration.asan.certificates.dto.AsanLoginRequest;

public interface AuthService {

    TokenResponseDto signIn(AsanLoginRequest asanLoginRequest);

    OrganizationTokenResponseDto setOrganization(OrganizationSelectionRequestDto request,
                                                 UserPrincipal principal);

    SimpleTokenResponseDto setUserRole(UserRoleRequestDto requestBody, UserPrincipal principal);
}
