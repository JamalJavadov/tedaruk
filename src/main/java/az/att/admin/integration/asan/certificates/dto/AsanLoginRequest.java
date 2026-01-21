package az.att.admin.integration.asan.certificates.dto;

import az.att.admin.enums.AuthType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Request body for /api/mygovid/full-tokens")
public class AsanLoginRequest {

    @Schema(description = "Login type", example = "ASAN_LOGIN", requiredMode = Schema.RequiredMode.REQUIRED)
    private AuthType type;

    @NotNull
    @Schema(description = "ASAN login specific information", requiredMode = Schema.RequiredMode.REQUIRED)
    private AsanLoginInfo asanLoginInfo;

    @Data
    @Schema(description = "ASAN login payload")
    public static class AsanLoginInfo {
        @NotBlank
        @Schema(description = "Authorization code from MyGovID", example = "AUTH_CODE", requiredMode = Schema.RequiredMode.REQUIRED)
        private String authCode;
    }
}
