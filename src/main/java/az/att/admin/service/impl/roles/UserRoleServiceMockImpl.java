package az.att.admin.service.impl.roles;

import az.att.admin.service.UserRoleService;
import az.att.admin.service.impl.roles.dto.PortalUserRole;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Profile("test")
public class UserRoleServiceMockImpl implements UserRoleService {

    @Override
    public List<PortalUserRole> findUserRoles(String userId, String tin) {
        return List.of(new PortalUserRole(1L, "ADMIN"), new PortalUserRole(2L, "USER"));
    }

    @Override
    public List<String> findPermissions(Long roleId) {
        return List.of("VIEW_SETTINGS");
    }
}
