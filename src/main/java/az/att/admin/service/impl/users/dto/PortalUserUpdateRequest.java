package az.att.admin.service.impl.users.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PortalUserUpdateRequest {

    private String documentNumber;
    private String phoneNumber;
    private String gmail;
    private String patronymic;
    private String citizenship;
}
