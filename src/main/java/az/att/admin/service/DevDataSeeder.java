package az.att.admin.service;

import az.att.admin.enums.Module;
import az.att.admin.entity.Organization;
import az.att.admin.entity.Permission;
import az.att.admin.entity.Role;
import az.att.admin.repository.OrganizationRepository;
import az.att.admin.repository.PermissionRepository;
import az.att.admin.repository.RoleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Profile("local")
@RequiredArgsConstructor
public class DevDataSeeder implements ApplicationRunner {

    private static final String VOEN = "1001101112";
    private static final String USER_ROLE = "USER";
    private static final String SEED_USER = "camal";

    private final RoleRepository roleRepository;
    private final OrganizationRepository organizationRepository;
    private final PermissionRepository permissionRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {

        Organization organization = organizationRepository.findOrganizationByTin(VOEN)
                .orElseGet(() -> {
                    Organization o = new Organization();
                    o.setTin(VOEN);
                    return organizationRepository.save(o);
                });

        Map<Module, Permission> permissionsByModule = Arrays.stream(Module.values())
                .map(this::createPermissionIfMissing)
                .collect(Collectors.toMap(Permission::getName, p -> p));

        roleRepository.findByNameAndOrganizationAndIsDefaultFalse(USER_ROLE, organization)
                .orElseGet(() -> {
                    Role r = new Role();
                    r.setName(USER_ROLE);
                    r.setCreatedBy(SEED_USER);
                    r.setLastModifiedBy(SEED_USER);
                    r.setFromSystem(false);
                    r.setOrganization(organization);
                    r.setDefault(false);

                    r.setPermissions(List.of(
                            permissionsByModule.get(Module.VIEW_DASHBOARD),
                            permissionsByModule.get(Module.VIEW_DEMANDS),
                            permissionsByModule.get(Module.VIEW_SETTINGS)
                    ));

                    return roleRepository.save(r);
                });
    }

    private Permission createPermissionIfMissing(Module module) {
        return permissionRepository.findByName(module)
                .map(existing -> {
                    if (!Objects.equals(existing.getLabel(), module.getLabel())) {
                        existing.setLabel(module.getLabel());
                        existing.setLastModifiedBy(SEED_USER);
                        return permissionRepository.save(existing);
                    }
                    return existing;
                })
                .orElseGet(() -> {
                    Permission p = new Permission();
                    p.setName(module);
                    p.setLabel(module.getLabel());
                    p.setCreatedBy(SEED_USER);
                    p.setLastModifiedBy(SEED_USER);
                    return permissionRepository.save(p);
                });
    }
}
