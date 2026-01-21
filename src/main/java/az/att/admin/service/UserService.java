package az.att.admin.service;

import az.att.admin.integration.asan.login.dto.AsanJwtResponse;
import az.att.admin.service.impl.users.dto.PortalUser;

public interface UserService {

    PortalUser createOrUpdateUser(AsanJwtResponse payload, String phone);
}
