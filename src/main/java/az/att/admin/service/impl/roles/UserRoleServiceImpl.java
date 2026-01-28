package az.att.admin.service.impl.roles;

import az.att.admin.entity.AsanUserCertificatesEntity;
import az.att.admin.entity.Organization;
import az.att.admin.entity.Permission;
import az.att.admin.entity.Role;
import az.att.admin.entity.UserRole;
import az.att.admin.repository.OrganizationRepository;
import az.att.admin.repository.PermissionRepository;
import az.att.admin.repository.RoleRepository;
import az.att.admin.repository.UserDetailRepository;
import az.att.admin.repository.UserRolesRepository;
import az.att.admin.service.UserRoleService;
import az.att.admin.service.impl.roles.dto.PortalUserRole;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Profile("local")
public class UserRoleServiceImpl implements UserRoleService {

    private static final String DIRECTOR_ROLE_NAME = "Möhür Səlahiyyətli";
    private static final String SYSTEM_USER = "system";

    private final RoleRepository roleRepository;
    private final UserRolesRepository userRolesRepository;
    private final UserDetailRepository userDetailRepository;
    private final PermissionRepository permissionRepository;
    private final OrganizationRepository organizationRepository;

    @Override
    @Transactional
    public List<PortalUserRole> findUserRoles(String userId, String tin) {
        UUID userUuid = parseUserId(userId);
        validateTin(tin);

        List<PortalUserRole> existingRoles = fetchUserRolesForTin(userUuid, tin);
        if (!existingRoles.isEmpty()) {
            return existingRoles;
        }

        return provisionDirectorRoleIfEligible(userUuid, tin)
                .map(List::of)
                .orElseThrow(() -> new EntityNotFoundException("Roles not found for userId/tin"));
    }

    @Override
    public List<String> findPermissions(Long roleId) {
        if (roleId == null) {
            return List.of();
        }

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new EntityNotFoundException("Role not found: " + roleId));

        List<Permission> permissions = role.getPermissions() == null ? List.of() : role.getPermissions();

        return permissions.stream()
                .map(Permission::getName)
                .filter(java.util.Objects::nonNull)
                .map(Enum::name)
                .toList();
    }

    private List<PortalUserRole> fetchUserRolesForTin(UUID userUuid, String tin) {
        List<UserRole> roles = userRolesRepository.findByTinAndAsanUserCertificateAsanUserId(tin, userUuid);
        return roles.stream()
                .map(this::toPortalUserRole)
                .toList();
    }

    private Optional<PortalUserRole> provisionDirectorRoleIfEligible(UUID userUuid, String tin) {
        AsanUserCertificatesEntity certificate = userDetailRepository.findByAsanUser_IdAndTin(userUuid, tin)
                .orElseThrow(() -> new EntityNotFoundException("Certificate not found for user/tin"));

        if (!isEligibleDirector(certificate)) {
            return Optional.empty();
        }

        Organization organization = organizationRepository.findOrganizationByTin(tin)
                .orElseThrow(() -> new EntityNotFoundException("Organization not found for voen: " + tin));

        Role role = createDirectorRoleIfMissing(organization);

        UserRole userRole = assignRoleToCertificate(role, certificate, tin);
        return Optional.of(toPortalUserRole(userRole));
    }

    @Transactional
    protected Role createDirectorRoleIfMissing(Organization organization) {
        return roleRepository.findByOrganizationIdAndName(organization.getId(), DIRECTOR_ROLE_NAME)
                .orElseGet(() -> createDirectorRole(organization));
    }

    private Role createDirectorRole(Organization organization) {
        List<Permission> permissions = permissionRepository.findAll();

        Role role = Role.builder()
                .name(DIRECTOR_ROLE_NAME)
                .createdBy(SYSTEM_USER)
                .organization(organization)
                .isFromSystem(false)
                .isDefault(false)
                .permissions(permissions)
                .build();

        return roleRepository.save(role);
    }

    @Transactional
    protected UserRole assignRoleToCertificate(Role role, AsanUserCertificatesEntity certificate, String tin) {
        UserRole userRole = new UserRole();
        userRole.setRole(role);
        userRole.setAsanUserCertificate(certificate);
        userRole.setTin(tin);
        return userRolesRepository.save(userRole);
    }

    private boolean isEligibleDirector(AsanUserCertificatesEntity certificate) {
        return Boolean.TRUE.equals(certificate.getHasStamp())
                && StringUtils.hasText(certificate.getPosition())
                && certificate.getPosition().startsWith("1");
    }

    private UUID parseUserId(String userId) {
        if (!StringUtils.hasText(userId)) {
            throw new IllegalArgumentException("userId is blank");
        }
        try {
            return UUID.fromString(userId);
        } catch (Exception e) {
            throw new IllegalArgumentException("userId is not a valid UUID");
        }
    }

    private void validateTin(String tin) {
        if (!StringUtils.hasText(tin)) {
            throw new IllegalArgumentException("tin is blank");
        }
    }

    private PortalUserRole toPortalUserRole(UserRole userRole) {
        return new PortalUserRole(userRole.getId(), userRole.getRole().getName());
    }


}
