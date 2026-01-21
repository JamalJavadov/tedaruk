package az.att.admin.service;


import az.att.admin.service.impl.roles.dto.PortalUserRole;

import java.util.List;

public interface UserRoleService {

    List<PortalUserRole> findUserRoles(String userId, String tin);

    List<String> findPermissions(Long roleId);
}
