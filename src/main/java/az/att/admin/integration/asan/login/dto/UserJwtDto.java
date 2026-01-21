package az.att.admin.integration.asan.login.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserJwtDto {
    private String pin;
    private String firstName;
    private String lastName;
    private String patronymic;
    private String citizenship;
    private String phone;
}
