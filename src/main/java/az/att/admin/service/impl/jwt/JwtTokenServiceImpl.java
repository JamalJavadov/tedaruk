package az.att.admin.service.impl.jwt;

import az.att.admin.config.auth.SecurityProperties;
import az.att.admin.config.auth.UserPrincipal;
import az.att.admin.service.impl.JwtTokenService;
import az.att.admin.service.impl.users.dto.PortalUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JwtTokenServiceImpl implements JwtTokenService {

    private final SecurityProperties securityProperties;
    private Key hmacKey;
    private JwtParser jwtParser;

    @PostConstruct
    public void init() {
        byte[] keyBytes = Decoders.BASE64.decode(
                securityProperties.getJwtProperties().getSecret());
        this.hmacKey = Keys.hmacShaKeyFor(keyBytes);
        this.jwtParser = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(keyBytes))
                .build();
    }

    @Override
    public String createAccessToken(PortalUser portalUser) {
        Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put("firstName", portalUser.getFirstName());
        claimsMap.put("lastName", portalUser.getLastName());
        claimsMap.put("pin", portalUser.getPin());
        return createTokenWithClaims(String.valueOf(portalUser.getId()), claimsMap,
                Duration.ofSeconds(securityProperties.getJwtProperties().getTokenValidityInSeconds()));
    }

    @Override
    public String createAccessToken(UserPrincipal principal, String tin) {
        Map<String, Object> claimsMap = convertToClaimsMap(principal);
        claimsMap.put("tin", tin);
        return buildToken(principal.getUserId(), claimsMap);
    }

    @Override
    public String createAccessToken(UserPrincipal principal, List<String> permissions) {
        Map<String, Object> claimsMap = convertToClaimsMap(principal);
        claimsMap.put(JwtClaims.PERMISSIONS, permissions);
        claimsMap.put("tin", principal.getTin());
        return buildToken(principal.getUserId(), claimsMap);
    }

    private String buildToken(String userId, Map<String, Object> claimsMap) {
        return createTokenWithClaims(
                userId,
                claimsMap,
                Duration.ofSeconds(securityProperties.getJwtProperties().getTokenValidityInSeconds()));
    }

    private String createTokenWithClaims(String subject, Map<String, Object> claims, Duration duration) {
        Instant now = Instant.now();
        JwtBuilder builder = Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(duration)))
                .setHeader(Map.of("typ", "JWT"))
                .signWith(hmacKey, SignatureAlgorithm.HS512);

        if (claims != null && !claims.isEmpty()) {
            builder.addClaims(claims);
        }
        return builder.compact();
    }

    @Override
    public Claims parseToken(String token) {
        Jws<Claims> jwsClaims = jwtParser.parseClaimsJws(token);
        return jwsClaims.getBody();
    }

    @Override
    public UserPrincipal extractPayloadFromClaims(Claims claims) {
        return buildJwtPayloadFromClaims(claims);
    }

    private Map<String, Object> convertToClaimsMap(UserPrincipal userPrincipal) {
        Map<String, Object> claimsMap = new HashMap<>();

        if (userPrincipal != null) {
            if (userPrincipal.getUserId() != null) {
                claimsMap.put(JwtClaims.USER_ID, userPrincipal.getUserId());
                claimsMap.put(JwtClaims.SUBJECT, userPrincipal.getUserId());
            }
            if (userPrincipal.getPin() != null) {
                claimsMap.put(JwtClaims.PIN, userPrincipal.getPin());
            }
            if (userPrincipal.getFirstName() != null) {
                claimsMap.put(JwtClaims.FIRST_NAME, userPrincipal.getFirstName());
            }
            if (userPrincipal.getLastName() != null) {
                claimsMap.put(JwtClaims.LAST_NAME, userPrincipal.getLastName());
            }
            if (userPrincipal.getTin() != null) {
                claimsMap.put(JwtClaims.TIN, userPrincipal.getTin());
            }
        }

        return claimsMap;
    }

    private UserPrincipal buildJwtPayloadFromClaims(Claims claims) {
        return UserPrincipal.builder()
                .userId(claims.getSubject())
                .pin(claims.get(JwtClaims.PIN, String.class))
                .firstName(claims.get(JwtClaims.FIRST_NAME, String.class))
                .lastName(claims.get(JwtClaims.LAST_NAME, String.class))
                .tin(claims.get(JwtClaims.TIN, String.class))
                .permissions(claims.get(JwtClaims.PERMISSIONS, List.class))
                .build();
    }
}