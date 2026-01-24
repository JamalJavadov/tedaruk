package az.att.admin.service.impl.roles.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleMiniResponseDto {
    private Long id;
    private String name;
    private boolean isFromSystem;
}
