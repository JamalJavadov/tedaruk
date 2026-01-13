package az.att.auth.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserJwtDto {
    private String pin;
    private String name;
    private String surname;
    private String patronymic;
    private String citizenship;
}
