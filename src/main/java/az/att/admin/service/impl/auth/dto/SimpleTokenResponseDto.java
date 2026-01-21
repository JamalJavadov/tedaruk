package az.att.admin.service.impl.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SimpleTokenResponseDto {
    private String accessToken;
    private String refreshToken;
}