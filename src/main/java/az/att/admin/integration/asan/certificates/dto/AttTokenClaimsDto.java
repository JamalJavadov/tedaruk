package az.att.admin.integration.asan.certificates.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttTokenClaimsDto {

    private String userId;
    private String pin;
    private String firstName;
    private String lastName;
    private String sub;
    private String tin;
}