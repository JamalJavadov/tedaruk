package az.att.auth.services;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public final class TokenAuthService implements AuthService {

    public static final String AUTHORITIES_CLAIM = "authorities";
    public static final String ACCESS_TOKEN_COOKIE = "jwt_accessToken";
    public static final String AUTHORIZATION = "Authorization";
    public static final String REFRESH_TOKEN_COOKIE = "refreshToken";
    public static final String TENANT_ID = "tenantId";
    public static final String USER_UUID = "userUuid";

    private final JwtService jwtService;

    @Override
    public Optional<Authentication> getAuthentication(HttpServletRequest httpServletRequest) {
        final Optional<String> header = getHeader(httpServletRequest);
        if (header.isPresent()) {
            return getAuthenticationBearer(header.get());
        }
        return Optional.empty();
    }

    private Optional<String> getHeader(HttpServletRequest httpServletRequest) {
        String header = httpServletRequest.getHeader(AUTHORIZATION);
        if (header == null)
            return Optional.empty();

        if (header.startsWith("Bearer")) {
            header = header.substring("Bearer".length()).trim();
        }
        return Optional.of(header);
    }

    private Optional<Cookie> getCookie(HttpServletRequest httpServletRequest) {
        Cookie[] cookies = Optional.ofNullable(httpServletRequest.getCookies())
                .orElse(new Cookie[0]);
        return Arrays.stream(cookies).filter(cookie -> cookie.getName().equals(ACCESS_TOKEN_COOKIE))
                .findFirst();
    }

    private Optional<Authentication> getAuthenticationBearer(String token) {
        log.trace("Jwt access token is : {}", token);

        Claims claims = null;
        try {
            claims = jwtService.parseToken(token);
        } catch (RuntimeException e) {
            //ignore
            log.warn("Exception while parsing jwt token", e);
        }

        log.trace("The claims parsed {}", claims);
        if (claims == null || claims.getExpiration().before(new Date())) {
            return Optional.empty();
        }
        return Optional.of(getAuthenticationBearer(claims));
    }

    private Authentication getAuthenticationBearer(Claims claims) {
        final Collection<? extends GrantedAuthority> userAuthorities = getUserAuthorities(claims);
        User userDetails = new User(claims.getSubject(), "", userAuthorities);
        String tenantId = Optional.ofNullable(claims.get(TENANT_ID))
                .map(Object::toString)
                .orElse("");
        String userId = Optional.ofNullable(claims.get(USER_UUID)).orElse("").toString(); //should be internal server error
        // in case of failure
        UserPrincipal userPrincipal = new UserPrincipal(userDetails, tenantId);
        return new UsernamePasswordAuthenticationToken(userPrincipal, "", userAuthorities);
    }

    private Collection<? extends GrantedAuthority> getUserAuthorities(Claims claims) {
        List<?> roles = claims.get(AUTHORITIES_CLAIM, List.class);
        return roles
                .stream()
                .map(a -> new SimpleGrantedAuthority(a.toString()))
                .collect(Collectors.toList());
    }
}
