package az.att.admin.integration.asan.login.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginDetailDto {
    @JsonProperty("phoneNumber")
    private String phoneNumber;

    @JsonProperty("userId")
    private String userId;
}
