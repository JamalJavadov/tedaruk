package az.att.admin.integration.iamas;

import az.att.admin.integration.iamas.dto.IdCardResponseDto;
import az.att.admin.integration.iamas.dto.IdCardSimpleResponseDto;
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
import org.springframework.web.util.UriComponentsBuilder;
import java.util.Optional;

@Slf4j
@Component
@Profile("local")
@RequiredArgsConstructor
public class IamasClientImpl implements IamasClient {

        private final RestTemplate restTemplate;

        @Value("${iamas.bridge.token}")
        private String bridgeToken;

        @Value("${iamas.bridge.url}")
        private String iamasUrl;

        @Override
        public Optional<IdCardSimpleResponseDto> getIdCardList(String docNumber, String pin) {

                String url = UriComponentsBuilder.fromHttpUrl(iamasUrl)
                                .queryParam("documentNumber", docNumber)
                                .queryParam("fin", pin)
                                .toUriString();

                HttpHeaders headers = new HttpHeaders();
                headers.set("X-Bridge-Authorization", bridgeToken);

                HttpEntity<Void> entity = new HttpEntity<>(headers);

                ResponseEntity<IdCardResponseDto> responseEntity = restTemplate.exchange(
                                url, HttpMethod.GET, entity, IdCardResponseDto.class);

                return Optional.ofNullable(responseEntity.getBody())
                                .map(IdCardResponseDto::getData)
                                .flatMap(data -> data.stream().findFirst())
                                .map(IdCardResponseDto.IdCardDataWrapperDto::getPersonAz)
                                .map(person -> IdCardSimpleResponseDto.builder()
                                                .name(person.getName())
                                                .surname(person.getSurname())
                                                .docNumber(docNumber)
                                                .pin(pin)
                                                .build());

        }
}
