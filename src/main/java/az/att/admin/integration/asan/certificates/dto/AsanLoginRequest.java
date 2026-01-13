package az.att.admin.integration.asan.certificates.dto;

import az.att.admin.service.AuthType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Request body for /api/mygovid/full-tokens")
public class AsanLoginRequest {

    @Schema(description = "Login type", example = "ASAN_LOGIN", required = true)
    private AuthType type;

    @NotNull
    @Schema(description = "ASAN login specific information", required = true)
    private AsanLoginInfo asanLoginInfo;

    @Data
    @Schema(description = "ASAN login payload")
    public static class AsanLoginInfo {

        @NotBlank
        @Schema(description = "Authorization code from MyGovID", example = "AUTH_CODE", required = true)
        private String authCode;
    }
}
