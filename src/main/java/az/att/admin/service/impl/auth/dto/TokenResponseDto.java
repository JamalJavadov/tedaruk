
package az.att.admin.service.impl.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenResponseDto {
  private String accessToken;
  private String refreshToken;
  private List<Organization> organizations;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Organization {
    private String tin;
    private String name;

  }

}