package az.att.admin.service.impl.permission.dto;

import az.att.admin.enums.Module;
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
public class PermissionResponseDto {
    private Module name;
    private String label;
}
