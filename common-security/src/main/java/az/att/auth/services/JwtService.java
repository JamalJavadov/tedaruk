package az.att.auth.services;

import az.att.auth.services.providers.ClaimProvider;
import az.att.auth.services.providers.ClaimSetProvider;
import az.att.config.SecurityProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public final class JwtService {

    private final Set<ClaimSetProvider> claimSetProviders;
    private final Set<ClaimProvider> claimProviders;
    private final SecurityProperties securityProperties;

    private Key key;

    @PostConstruct
    public void init() {
        byte[] keyBytes;
        keyBytes = Decoders.BASE64.decode(securityProperties.getJwtProperties().getSecret());
        key = Keys.hmacShaKeyFor(keyBytes);
    }

    public Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String issueToken(Authentication authentication, Duration duration) {
        log.trace("Issue JWT token to {} for {}", authentication.getPrincipal(), duration);
        final JwtBuilder jwtBuilder = Jwts.builder()
                .setSubject(authentication.getName())
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plus(Duration.ofSeconds(securityProperties.getJwtProperties()
                        .getTokenValidityInSeconds()))))
                .setHeader(Map.of("type", "JWT"))
                .addClaims(Map.of("authorities", authentication.getAuthorities()))
                .signWith(key, SignatureAlgorithm.HS512);
        addClaimsSets(jwtBuilder, authentication);
        addClaims(jwtBuilder, authentication);
        return jwtBuilder.compact();
    }

    private JwtBuilder addClaimsSets(JwtBuilder jwtBuilder, Authentication authentication) {
        claimSetProviders.forEach(claimSetProvider -> {
            final ClaimSet claimSet = claimSetProvider.provide(authentication);
            log.trace("Adding claim {}", claimSet);
            jwtBuilder.claim(claimSet.getKey(), claimSet.getClaims());
        });
        return jwtBuilder;
    }

    private JwtBuilder addClaims(JwtBuilder jwtBuilder, Authentication authentication) {
        claimProviders.forEach(claimSetProvider -> {
            final Claim claim = claimSetProvider.provide(authentication);
            log.trace("Adding claim {}", claim);
            jwtBuilder.claim(claim.getKey(), claim.getClaim());
        });
        return jwtBuilder;
    }

}
