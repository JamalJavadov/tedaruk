package az.att.admin.service.impl.roles.dto;

import az.att.admin.enums.Module;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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
public class RoleUpdateDto {
    @NotBlank
    private String name;

    @NotEmpty
    private List<Module> permissionNames;
}
