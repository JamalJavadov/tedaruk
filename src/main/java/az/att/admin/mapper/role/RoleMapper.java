package az.att.admin.mapper.role;

import az.att.admin.service.impl.roles.dto.RoleCreateDto;
import az.att.admin.service.impl.roles.dto.RoleMiniResponseDto;
import az.att.admin.service.impl.roles.dto.RoleResponseDto;
import az.att.admin.service.impl.roles.dto.RoleUpdateDto;
import az.att.admin.entity.Role;
import az.att.admin.mapper.permission.PermissionMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {PermissionMapper.class})
public interface RoleMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "organization", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "permissions", ignore = true)
    @Mapping(target = "isDefault", ignore = true)
    @Mapping(target = "isFromSystem", ignore = true)
    Role toEntity(RoleCreateDto dto);

    RoleMiniResponseDto toMiniDto(Role role);

    RoleResponseDto toDto(Role role);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "organization", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "permissions", ignore = true)
    @Mapping(target = "default", ignore = true)
    @Mapping(target = "fromSystem", ignore = true)
    void update(@MappingTarget Role role, RoleUpdateDto dto);
}
