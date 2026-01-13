package az.att.auth.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenClaimsDto {
    private String sub;
    private String role;
    private Boolean isActive;
    private Object authorities;
}
