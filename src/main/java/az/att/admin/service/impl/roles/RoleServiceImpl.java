package az.att.admin.service.impl.roles;

import az.att.admin.config.auth.UserPrincipal;
import az.att.admin.entity.Organization;
import az.att.admin.entity.Permission;
import az.att.admin.entity.Role;
import az.att.admin.enums.Module;
import az.att.admin.mapper.role.RoleMapper;
import az.att.admin.repository.OrganizationRepository;
import az.att.admin.repository.PermissionRepository;
import az.att.admin.repository.RoleRepository;
import az.att.admin.service.RoleService;
import az.att.admin.service.impl.roles.dto.RoleCreateDto;
import az.att.admin.service.impl.roles.dto.RoleMiniResponseDto;
import az.att.admin.service.impl.roles.dto.RoleResponseDto;
import az.att.admin.service.impl.roles.dto.RoleUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@PreAuthorize("hasAnyAuthority('VIEW_SETTINGS')")
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;
    private final PermissionRepository permissionRepository;
    private final OrganizationRepository organizationRepository;

    @Transactional
    public RoleMiniResponseDto create(RoleCreateDto dto, UserPrincipal userPrincipal) {
        Organization organization = findOrganization(userPrincipal.getTin());

        List<Permission> permissions = getPermissions(dto.getPermissionNames());

        Role role = roleMapper.toEntity(dto);
        role.setCreatedBy(userPrincipal.getPin());
        role.setOrganization(organization);
        role.setPermissions(permissions);

        return roleMapper.toMiniDto(roleRepository.save(role));
    }

    @Transactional
    public Page<RoleMiniResponseDto> listRoles(Pageable pageable, UserPrincipal userPrincipal) {
        return roleRepository.findAllByIsDefaultFalseAndOrganizationId(findOrganization(userPrincipal.getTin()).getId(), pageable).map(roleMapper::toMiniDto);
    }


    @Transactional(readOnly = true)
    public RoleResponseDto getById(long id) {
        Role role = roleRepository.findByIdAndIsDefaultFalse(id).orElseThrow(() -> new RuntimeException("Role not found for id=" + id));

        return roleMapper.toDto(role);
    }

    @Transactional
    public void softDelete(long id, UserPrincipal userPrincipal) {
        int updated = roleRepository.softDeleteToDefaultByIdAndTin(id, userPrincipal.getTin(), userPrincipal.getPin());
        if (updated == 0) {
            throw new RuntimeException("Role not found or not allowed to delete");
        }
    }

    @Transactional
    public RoleMiniResponseDto update(RoleUpdateDto dto, Long id, UserPrincipal userPrincipal) {
        Role role = roleRepository.findByIdAndIsDefaultFalse(id).orElseThrow(() -> new RuntimeException("Role not found for id=" + id));

        roleMapper.update(role, dto);
        role.setPermissions(getPermissions(dto.getPermissionNames()));
        role.setLastModifiedBy(userPrincipal.getPin());

        Role saved = roleRepository.save(role);
        return roleMapper.toMiniDto(saved);
    }

    private List<Permission> getPermissions(List<Module> modules) {
        List<Permission> permissions = permissionRepository.findAllById(modules);
        if (permissions.size() != modules.size()) {
            throw new RuntimeException("Permission modules not found for values=" + modules);
        }
        return permissions;
    }

    private Organization findOrganization(String tin) {
        return organizationRepository.findOrganizationByTin(tin).orElseThrow(() -> new RuntimeException("Organization not found for voen=" + tin));
    }

}
