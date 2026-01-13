package az.att.admin.web;

import az.att.admin.integration.asan.certificates.dto.AsanLoginRequest;
import az.att.admin.dto.TokenResponseDto;
import az.att.admin.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping(path = "/auth/sign-in")
    public ResponseEntity<TokenResponseDto> signIn(@RequestBody @Valid AsanLoginRequest requestBody) {
        return ResponseEntity.ok(authService.signIn(requestBody));
    }
}
