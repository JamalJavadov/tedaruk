package az.att.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Getter
@Configuration
@ConfigurationProperties(prefix = "security")
public class SecurityProperties {

    private final JwtProperties jwtProperties = new JwtProperties();

    @Getter
    @Setter
    private List<String> allowedRedirectHosts;

    @Getter
    @Setter
    public static class JwtProperties {

        private String secret;
        private long tokenValidityInSeconds;
        private long refreshTokenValidityInSeconds;
    }
}
