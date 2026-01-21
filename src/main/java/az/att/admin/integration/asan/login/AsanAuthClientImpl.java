package az.att.admin.integration.asan.login;

import az.att.admin.integration.asan.certificates.dto.AsanLoginRequest;
import az.att.admin.integration.asan.login.dto.AsanJwtResponse;
import az.att.admin.integration.asan.login.dto.AsanLoginResponse;
import az.att.admin.integration.asan.login.jwt.AsanJwtService;
import az.att.exception.ApplicationException;
import io.jsonwebtoken.Claims;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static az.att.exception.CommonErrors.HTTP_401;

@Slf4j
@Component
@Profile("!local")
@RequiredArgsConstructor
public class AsanAuthClientImpl implements AsanAuthClient {

    private final RestTemplate restTemplate;

    private final AsanJwtService jwtService;

    @Value("${mygovid.client.id}")
    private String clientId;

    @Value("${mygovid.client.secret}")
    private String clientSecret;

    @Value("${mygovid.redirect.uri}")
    private String redirectUri;

    @Value("${mygovid.token.url:https://apitest.mygovid.gov.az/ssoauth/oauth2/token}")
    private String tokenUrl;

    @Value("${mygovid.jwks.url:https://apitest.mygovid.gov.az/ssoauth/oauth2/jwks}")
    private String jwksUrl;

    public AsanLoginResponse authenticate(AsanLoginRequest asanLoginRequest) {
        String authCode = asanLoginRequest.getAsanLoginInfo().getAuthCode();
        AsanLoginResponse asanLoginResponse = makeLoginRequest(authCode);
        validateLoginResponse(asanLoginResponse);
        return asanLoginResponse;
    }

    public AsanJwtResponse parseToken(AsanLoginResponse asanLoginResponse) {
        try {
            String jwksJson = getJwksJson();
            Claims claims = jwtService.verifyJwt(asanLoginResponse.getIdToken(), jwksJson);
            return jwtService.convertClaimsToDto(claims, AsanJwtResponse.class);
        } catch (Exception e) {
            log.error("Failed to verify id_token", e);
            throw new ApplicationException(HTTP_401, e);
        }
    }

    private AsanLoginResponse makeLoginRequest(@NotNull String authCode) {
        if (StringUtils.isBlank(authCode)) {
            throw new IllegalArgumentException("Asan auth code is null or blank");
        }
        HttpEntity<?> request = new HttpEntity<>(buildRequestBody(authCode), buildRequestHeaders());
        ResponseEntity<AsanLoginResponse> response = restTemplate.exchange(tokenUrl, HttpMethod.POST, request,
                AsanLoginResponse.class);
        return response.getBody();
    }

    private MultiValueMap<String, String> buildRequestBody(String authCode) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("redirect_uri", redirectUri);
        body.add("code", authCode);
        return body;
    }

    private HttpHeaders buildRequestHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Basic " + createBasicAuthToken());
        return headers;
    }

    private String createBasicAuthToken() {
        return Base64.getEncoder()
                .encodeToString((clientId + ":" + clientSecret)
                        .getBytes(StandardCharsets.UTF_8));
    }

    private void validateLoginResponse(AsanLoginResponse asanLoginResponse) {
        if (asanLoginResponse == null || asanLoginResponse.getIdToken() == null) {
            log.warn("Login response is id token is null {}", asanLoginResponse);
            throw new ApplicationException(HTTP_401);
        }
    }

    private String getJwksJson() {
        HttpEntity<Void> request = new HttpEntity<>(new HttpHeaders());
        ResponseEntity<String> response = restTemplate.exchange(jwksUrl, HttpMethod.GET, request, String.class);
        return response.getBody();
    }
}
