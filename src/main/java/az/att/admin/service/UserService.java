package az.att.admin.service;

import az.att.admin.config.auth.UserPrincipal;
import az.att.admin.integration.asan.login.dto.AsanJwtResponse;
import az.att.admin.service.impl.users.dto.PortalUser;
import az.att.admin.service.impl.users.dto.PortalUserCreateRequest;
import az.att.admin.service.impl.users.dto.PortalUserUpdateRequest;
import az.att.admin.service.impl.users.dto.UserContactUpdateRequest;
import az.att.admin.service.impl.users.dto.UserInfoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UserService {

    PortalUser createOrUpdateUser(AsanJwtResponse payload, String phone);

    UserInfoResponse getUserInfo(UserPrincipal userPrincipal);
    PortalUser updateUserContact(UserPrincipal userPrincipal, UserContactUpdateRequest request);

    PortalUser createUser(PortalUserCreateRequest request);

    PortalUser getUser(UUID id);

    Page<PortalUser> listUsers(UserPrincipal userPrincipal, Pageable pageable);

    PortalUser updateUser(UUID id, PortalUserUpdateRequest request);

    void deleteUser(UUID id);
}
