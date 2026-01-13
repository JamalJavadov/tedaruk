package az.att.admin.integration.asan.certificates;

import az.att.admin.integration.asan.certificates.dto.AsanCertificatesResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@Profile("!local")
@RequiredArgsConstructor
public class AsanCertificatesClientImpl implements AsanCertificatesClient {

    @Value("${mygovid.resource.base-url:https://apitest.mygovid.gov.az/sso-oauth-resource}")
    private String resourceBaseUrl;

    private final RestTemplate restTemplate;

    @Override
    public AsanCertificatesResponseDto getCertificates(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<AsanCertificatesResponseDto> response = restTemplate.exchange(
                resourceBaseUrl + "/certificates",
                HttpMethod.GET,
                request,
                AsanCertificatesResponseDto.class);
        return response.getBody();
    }
}
