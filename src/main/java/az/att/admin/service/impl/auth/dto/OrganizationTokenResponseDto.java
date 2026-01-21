package az.att.admin.service.impl.auth.dto;

import az.att.admin.service.impl.roles.dto.PortalUserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationTokenResponseDto {
    private String accessToken;
    private String refreshToken;
    private List<PortalUserRole> userRoles;
}
