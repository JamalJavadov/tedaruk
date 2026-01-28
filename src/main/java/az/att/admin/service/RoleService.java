package az.att.admin.service;

import az.att.admin.config.auth.UserPrincipal;
import az.att.admin.service.impl.roles.dto.RoleCreateDto;
import az.att.admin.service.impl.roles.dto.RoleMiniResponseDto;
import az.att.admin.service.impl.roles.dto.RoleResponseDto;
import az.att.admin.service.impl.roles.dto.RoleUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RoleService {

    RoleMiniResponseDto create(RoleCreateDto dto, UserPrincipal userPrincipal);

    Page<RoleMiniResponseDto> listRoles(Pageable pageable, UserPrincipal userPrincipal);

    RoleResponseDto getById(long id);

    void softDelete(long id, UserPrincipal userPrincipal);

    RoleMiniResponseDto update(RoleUpdateDto dto, Long id, UserPrincipal userPrincipal);
}
