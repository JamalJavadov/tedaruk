package az.att.admin.integration.iamas;

import az.att.admin.integration.iamas.dto.IdCardSimpleResponseDto;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
@Profile("!local")
public class IamasClientMockImpl implements IamasClient {

    @Override
    public Optional<IdCardSimpleResponseDto> getIdCardList(String docNumber, String pin) {
        return Optional.of(IdCardSimpleResponseDto.builder()
                .name("İLHAM")
                .surname("SƏFƏROV")
                .docNumber(docNumber)
                .pin(pin)
                .build());
    }

}
