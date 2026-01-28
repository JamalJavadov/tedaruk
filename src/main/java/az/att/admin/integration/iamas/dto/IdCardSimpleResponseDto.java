package az.att.admin.integration.iamas.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IdCardSimpleResponseDto {
    private String name;
    private String surname;
    private String docNumber;
    private String pin;
}
