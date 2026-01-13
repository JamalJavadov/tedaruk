package az.att.admin.integration.asan.login.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AsanLoginRequest {

    @JsonProperty("grant_type")
    String grantType;

    @JsonProperty("redirect_uri")
    String redirectUrl;

    String code;
}
