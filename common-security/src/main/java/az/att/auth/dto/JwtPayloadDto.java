package az.att.auth.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class JwtPayloadDto {
    private String sub;
    private UserJwtDto user;
    private Long iat;
    private Long exp;
    private String type;
    private String role;
    private Boolean is_active;
}
