package az.att.admin.web;

import az.att.admin.config.auth.UserPrincipal;
import az.att.admin.service.AuthService;
import az.att.admin.service.impl.auth.dto.OrganizationSelectionRequestDto;
import az.att.admin.service.impl.auth.dto.OrganizationTokenResponseDto;
import az.att.admin.service.impl.auth.dto.UserRoleRequestDto;
import az.att.admin.service.impl.auth.dto.TokenResponseDto;
import az.att.admin.integration.asan.certificates.dto.AsanLoginRequest;
import az.att.admin.service.impl.auth.dto.SimpleTokenResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sign-in")
    public ResponseEntity<TokenResponseDto> signIn(@RequestBody @Valid AsanLoginRequest data) {
        return ResponseEntity.ok(authService.signIn(data));
    }

    @PostMapping("/access-token/set-organization")
    public ResponseEntity<OrganizationTokenResponseDto> setOrganization(
            @RequestBody @Valid OrganizationSelectionRequestDto data, Authentication authentication) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        return ResponseEntity.ok(authService.setOrganization(data, principal));
    }

    @PostMapping("/access-token/set-role")
    public ResponseEntity<SimpleTokenResponseDto> setRole(
            @RequestBody @Valid UserRoleRequestDto data, Authentication authentication) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        return ResponseEntity.ok(authService.setUserRole(data, principal));
    }
}