package az.att.admin.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RefreshTokenClaimsDto {
    private String sub;
    private String role;
    private Boolean isActive;
}
