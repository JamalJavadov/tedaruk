package az.att.admin.service.impl.users.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PortalUserCreateRequest {

    @NotBlank
    private String pin;

    @NotBlank
    private String documentNumber;

    private String phoneNumber;
    private String gmail;
    private String patronymic;
    private String citizenship;
}
