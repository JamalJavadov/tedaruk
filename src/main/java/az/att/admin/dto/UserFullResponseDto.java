package az.att.admin.dto;

import az.att.admin.integration.asan.login.dto.StructureDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserFullResponseDto {
    private UserResponseDto user;
    private Boolean isActive;
    private String role;
    private StructureDto structure;
}
