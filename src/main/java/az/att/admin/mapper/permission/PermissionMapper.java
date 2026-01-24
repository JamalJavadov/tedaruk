package az.att.admin.mapper.permission;

import az.att.admin.service.impl.permission.dto.PermissionResponseDto;
import az.att.admin.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    PermissionResponseDto toDto(Permission permission);
}
