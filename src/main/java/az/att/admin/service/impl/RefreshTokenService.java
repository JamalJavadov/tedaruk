package az.att.admin.service.impl;

import az.att.admin.integration.asan.login.dto.AsanJwtResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    public String createRefreshToken(AsanJwtResponse parsedIdToken) {
        return null;
    }
}
