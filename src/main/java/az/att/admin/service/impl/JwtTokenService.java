package az.att.admin.service.impl;

import az.att.admin.config.auth.UserPrincipal;
import az.att.admin.service.impl.users.dto.PortalUser;
import io.jsonwebtoken.Claims;

import java.util.List;

public interface JwtTokenService {

    String createAccessToken(PortalUser portalUser);

    String createAccessToken(UserPrincipal principal, String tin);

    String createAccessToken(UserPrincipal principal, List<String> permissions);

    Claims parseToken(String token);

    UserPrincipal extractPayloadFromClaims(Claims claims);
}
