package az.att.admin.service;

import az.att.admin.config.auth.UserPrincipal;
import az.att.admin.integration.asan.login.dto.AsanJwtResponse;
import az.att.admin.service.impl.users.dto.PortalUser;
import az.att.admin.service.impl.users.dto.UserInfoResponse;

public interface UserService {

    PortalUser createOrUpdateUser(AsanJwtResponse payload, String phone);

    UserInfoResponse getUserInfo(UserPrincipal userPrincipal);
}
