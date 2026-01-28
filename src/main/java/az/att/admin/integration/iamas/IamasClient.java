package az.att.admin.integration.iamas;

import az.att.admin.integration.iamas.dto.IdCardSimpleResponseDto;
import java.util.Optional;

public interface IamasClient {
    Optional<IdCardSimpleResponseDto> getIdCardList(String docNumber, String pin);
}
