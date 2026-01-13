package az.att.config;

import az.att.UserRole;
import az.att.auth.JwtAuthFilterConfigurerAdapter;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.Globals;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.net.URI;
import java.util.StringJoiner;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
import org.springframework.context.annotation.Configuration;

@Slf4j
//@Configuration  // Disabled - using SecurityConfig instead
@RequiredArgsConstructor
public class BaseSecurityConfig {

    private static final String ACTUATOR = "/actuator/**";
    private static final String SWAGGER2 = "/v2/api-docs";
    private static final String SWAGGER3 = "/v3/api-docs";
    private static final String SWAGGER_UI = "/swagger-ui/**";
    private static final String SWAGGER_HTML = "/swagger-ui.html";

    private final SecurityProperties securityProperties;
    private final JwtAuthFilterConfigurerAdapter authFilterConfigurerAdapter;
    private final ApplicationSecurityConfig securityConfig;

    //@Bean  // Disabled - using SecurityConfig instead
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        log.trace("Initializing base security config");
        http.apply(authFilterConfigurerAdapter);
        return http.csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .headers(headers -> headers
                        .referrerPolicy(rp -> rp.policy(org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter.ReferrerPolicy.NO_REFERRER_WHEN_DOWNGRADE))
                        .contentSecurityPolicy(csp -> csp
                                .policyDirectives("default-src 'self'; img-src 'self' data: https:; script-src 'self'; style-src 'self' 'unsafe-inline'"))
                ).authorizeHttpRequests(request -> {
                    securityConfig.configure(request);
                    request.anyRequest().denyAll();
                }).sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS))
                .build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(
                "/v3/api-docs/**",
                "/v2/api-docs/**",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/**",
                "/swagger-ui.html",
                "/swagger-ui/index.html",
                "/webjars/**",
                "/csrf",
                "/",
                "/swagger-ui/**"
        );
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration());
        return source;
    }


    @Bean
    public CorsConfiguration corsConfiguration() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        if (!Globals.IS_SECURITY_ENABLED) {
            corsConfiguration.addAllowedOrigin("*");
            corsConfiguration.setAllowCredentials(false);
        } else {
            corsConfiguration.addAllowedOrigin("http://localhost:4000");
            corsConfiguration.addAllowedOrigin("http://localhost:8000");
            corsConfiguration.addAllowedOrigin("https://ingress-hub.vercel.app");
            corsConfiguration.addAllowedMethod(HttpMethod.GET);
            corsConfiguration.addAllowedOrigin("");
            corsConfiguration.setAllowCredentials(true);
        }

        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.setMaxAge(3600L);
        return corsConfiguration;
    }

    protected String authority(String role) {
        return "hasAuthority('" + role + "')";
    }

    protected String authority(UserRole role) {
        return "hasAuthority('" + role.name() + "')";
    }

    protected String authorities(Object... roles) {
        StringJoiner joiner = new StringJoiner(" or ");
        for (Object role : roles) {
            if (role instanceof UserRole) {
                joiner.add(authority((UserRole) role));
            } else {
                joiner.add(authority(role.toString()));
            }
        }
        return joiner.toString();
    }

    @Bean
    AuthenticationSuccessHandler successHandler() {
        var fallback = new SavedRequestAwareAuthenticationSuccessHandler();
        fallback.setDefaultTargetUrl("/"); // backend fallback if no redirect is provided

        return (request, response, authentication) -> {
            HttpSession session = request.getSession(false);
            String target = (session != null) ? (String) session.getAttribute(CaptureRedirectParamFilter.ATTR) : null;
            if (session != null) session.removeAttribute(CaptureRedirectParamFilter.ATTR);

            if (isAllowed(target)) {
                response.sendRedirect(target);
            } else {
                fallback.onAuthenticationSuccess(request, response, authentication);
            }
        };
    }

    private boolean isAllowed(String url) {
        if (url == null || url.isBlank()) return false;
        try {
            URI u = URI.create(url);
            String host = u.getHost();
            String scheme = u.getScheme();
            if (host == null || scheme == null) return false;

            boolean schemeOk = scheme.equals("https") || (scheme.equals("http") && (host.equals("localhost") || host.equals("127.0.0.1")));
            if (!schemeOk) return false;

            return securityProperties.getAllowedRedirectHosts().stream().anyMatch(allowed ->
                    host.equalsIgnoreCase(allowed) || host.toLowerCase().endsWith("." + allowed.toLowerCase()));
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
