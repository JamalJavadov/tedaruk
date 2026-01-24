package az.att.admin.service.impl.roles.dto;

import az.att.admin.service.impl.permission.dto.PermissionResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleResponseDto {

    private Long id;
    private String name;
    private List<PermissionResponseDto> permissions;

}
