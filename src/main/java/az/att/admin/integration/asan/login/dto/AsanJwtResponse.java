package az.att.admin.integration.asan.login.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AsanJwtResponse {
    private String sub;
    private UserJwtDto user;
    private Long iat;
    private Long exp;
    private String type;
    private String role;
    private Boolean is_active;
}
